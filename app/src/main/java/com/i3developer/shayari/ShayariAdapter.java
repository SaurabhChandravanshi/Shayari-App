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
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ShayariAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int TEXT_STYLE = 0;
    private List<Object> dataList;
    private static final int AD_VIEW = 0;
    private static final int RECYCLER_VIEW = 1;
    private static int CURRENT_BACKGROUND = 0;
    private static final int MAX_COLOR = 10;
    public ShayariAdapter(List<Object> dataList) {
        this.dataList = dataList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==AD_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_ad_shayari_list,parent,false);
            return new MyAdViewHolder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shayari_list, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int pos) {
        if(pos!=0) {
            if(pos%3==0) {
                return AD_VIEW;
            }
            return RECYCLER_VIEW;
        }
        return RECYCLER_VIEW;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(dataList.get(position) instanceof AdView) {
            MyAdViewHolder myAdViewHolder = (MyAdViewHolder)holder;
            myAdViewHolder.adView.loadAd(new AdRequest.Builder().build());
        } else {
            Shayari data = (Shayari)dataList.get(position);
            MyViewHolder viewHolder = (MyViewHolder)holder;
            viewHolder.shayariTtv.setText(data.getQ());
            Shader shader = new LinearGradient(0f, 0f, 0f, viewHolder.shayariTtv.getTextSize(),
                    Color.RED, Color.BLUE, Shader.TileMode.CLAMP);
            viewHolder.shayariTtv.getPaint().setShader(shader);

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

        private AdView adView;
        public MyAdViewHolder(@NonNull View itemView) {
            super(itemView);
            adView = itemView.findViewById(R.id.recycler_ad_shayari_list_adView);
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
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book app for more\nhttps://shayariapp.page.link/install");
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
            default:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.colorRed));
        }
    }
}
