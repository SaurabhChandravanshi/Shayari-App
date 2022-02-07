package com.i3developer.shayari;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ShayariAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int TEXT_STYLE = 0;
    private List<Object> dataList;
    private static final int AD_VIEW = 0;
    private static final int RECYCLER_VIEW = 1;
    private static int CURRENT_BACKGROUND = 0;
    private static final int MAX_COLOR = 11;
    public ShayariAdapter(List<Object> dataList) {
        this.dataList = dataList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==AD_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_native_layout,parent,false);
            return new MyAdViewHolder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shayari_list, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int pos) {
        if (pos == 0)
            return RECYCLER_VIEW;
        else {
            if (pos % 6 == 0)
                return AD_VIEW;
            return RECYCLER_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(dataList.get(position) instanceof AdView) {
            MyAdViewHolder myAdViewHolder = (MyAdViewHolder)holder;
            myAdViewHolder.refreshAd(myAdViewHolder.itemView);
        } else {
            Shayari data = (Shayari)dataList.get(position);
            MyViewHolder viewHolder = (MyViewHolder)holder;
            viewHolder.shayariTtv.setText(data.getQ().trim().replaceFirst(" {2}","\n"));
            Shader shader = new LinearGradient(0f, 0f, 0f, viewHolder.shayariTtv.getTextSize(),
                    Color.RED, viewHolder.itemView.getContext().getResources().getColor(R.color.darkBlue), Shader.TileMode.CLAMP);
            viewHolder.shayariTtv.getPaint().setShader(shader);
            Typeface typeface = ResourcesCompat.getFont(viewHolder.itemView.getContext(),R.font.mukta_bold);
            viewHolder.shayariTtv.setTypeface(typeface);
            viewHolder.copyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipboard(viewHolder.itemView.getContext(),"Shayari",data.getQ());
                }
            });

            viewHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,data.getQ());
                    viewHolder.itemView.getContext().startActivity(intent);
                }
            });
            viewHolder.waBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setPackage("com.whatsapp");
                    intent.putExtra(Intent.EXTRA_TEXT,data.getQ());
                    try {
                        viewHolder.itemView.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        showToast(viewHolder.itemView.getContext(),"WhatsApp is not Installed.");
                    }
                }
            });
            viewHolder.editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Activity activity = (Activity)viewHolder.itemView.getContext();
                        LayoutInflater inflater = activity.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_editor_layout,null);
                        Button closeBtn = dialogView.findViewById(R.id.editor_close_btn);
                        EditText content = dialogView.findViewById(R.id.editor_content);
                        Typeface typeface = ResourcesCompat.getFont(viewHolder.itemView.getContext(),R.font.mukta_bold);
                        content.setTypeface(typeface);
                        content.setText(data.getQ());
                        CardView cardView = dialogView.findViewById(R.id.editor_card);
                        changeBackground(viewHolder.itemView.getContext(),cardView);
                        builder.setView(dialogView);
                        AlertDialog alertDialog = builder.create();
                        closeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                        Button shareBtn = dialogView.findViewById(R.id.editor_share_btn);
                        shareBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                content.setCursorVisible(false);
                                saveImageToCache(viewHolder.itemView.getContext(),viewToBitmap(cardView));
                                shareImage(viewHolder.itemView.getContext());
                            }
                        });
                        Button bgBtn = dialogView.findViewById(R.id.editor_bg_btn);
                        bgBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                changeBackground(viewHolder.itemView.getContext(),cardView);
                            }
                        });
                        alertDialog.show();
                    } else {

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView shayariTtv;
        private Button copyBtn,shareBtn,waBtn,editImage;
        public MyViewHolder(View itemView) {
            super(itemView);
            shayariTtv = itemView.findViewById(R.id.shayari_list_content);
            copyBtn = itemView.findViewById(R.id.shayari_list_copy_btn);
            waBtn = itemView.findViewById(R.id.shayari_list_wa_btn);
            shareBtn = itemView.findViewById(R.id.shayari_list_share_btn);
            editImage = itemView.findViewById(R.id.shayari_list_edit_image);
        }
    }
    class MyAdViewHolder extends RecyclerView.ViewHolder {
        private NativeAd mNativeAd;

        public MyAdViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void refreshAd(View view) {
            AdLoader.Builder builder = new AdLoader.Builder(view.getContext(), view.getContext().getString(R.string.native_ad_unit));
            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                @Override
                public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                    if (mNativeAd != null) {
                        mNativeAd.destroy();
                    }
                    mNativeAd = nativeAd;
                    FrameLayout frameLayout = view.findViewById(R.id.recycler_banner_frame);
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(view.getContext()).inflate(R.layout.native_ad, null);
                    populateNativeAdView(nativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            });
            NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
            builder.withNativeAdOptions(adOptions);
            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    CardView cardView = view.findViewById(R.id.recycler_banner_card);
                    cardView.setVisibility(View.VISIBLE);
                }
            }).build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
        private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
            adView.setMediaView(adView.findViewById(R.id.ad_media));
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            ((TextView)adView.getHeadlineView()).setText(nativeAd.getHeadline());
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
            if (nativeAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }
            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }
            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {

                ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
            adView.setNativeAd(nativeAd);
        }
    }
    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    private void copyToClipboard(Context context,String label,String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        showToast(context,"Copied to Clipboard");
    }
    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    private void saveImageToCache(Context context,Bitmap bitmap) {
        try {
            File cacheFile = new File(context.getCacheDir(),"images");
            cacheFile.mkdir();
            FileOutputStream output = new FileOutputStream(cacheFile+"/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void shareImage(Context context) {
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.i3developer.shayari.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Get the Shayari App\n"+context.getResources().getString(R.string.app_store_link));
            context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }


    private void changeBackground(Context context,CardView view) {
        if(CURRENT_BACKGROUND == MAX_COLOR) {
            CURRENT_BACKGROUND = 1;
        } else {
            CURRENT_BACKGROUND++;
        }
        switch (CURRENT_BACKGROUND) {
            case 1:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
                break;
            case 2:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color2));
                break;
            case 3:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color3));
                break;
            case 4:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color4));
                break;
            case 5:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color5));
                break;
            case 6:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color6));
                break;
            case 7:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color7));
                break;
            case 8:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color8));
                break;
            case 9:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color9));
                break;
            case 10:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color10));
                break;
            case 11:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlack));
                break;
            default:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.colorRed));
        }
    }
}
