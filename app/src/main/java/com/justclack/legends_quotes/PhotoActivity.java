package com.justclack.legends_quotes;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {
    int RC_IMG_CHOOSE = 1212;
    Uri imageURI = null;
    ImageView chooseImg;
    RecyclerView colorsList;
    TextView title, share;
    Typeface typeface, fontawesome;
    SeekBar simpleSeekBar;
    RelativeLayout screenshot;
    ArrayList<QuotesModelClass> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        colorsList = findViewById(R.id.colorsList);
        simpleSeekBar = findViewById(R.id.seekBar);
        screenshot = findViewById(R.id.screenshot);
        share = findViewById(R.id.share);
        colorsList.hasFixedSize();
        colorsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        chooseImg = findViewById(R.id.photo);
        title = findViewById(R.id.title);
        typeface = Typeface.createFromAsset(getAssets(), "NotoSerif-Regular.ttf");
        fontawesome = Typeface.createFromAsset(getAssets(), "fontawesome.ttf");
        share.setTypeface(fontawesome);
        share.setText("\uf1e0");
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = loadBitmapFromView(screenshot);
                checkPermission(bitmap, getIntent().getStringExtra("title"));
            }
        });
        title.setTypeface(typeface);
        title.setText(getIntent().getStringExtra("title"));
        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choose = new Intent(Intent.ACTION_GET_CONTENT);
                choose.setType("image/*");
                startActivityForResult(choose, RC_IMG_CHOOSE);
            }
        });
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                title.setTextSize(progressChangedValue);
                Toast.makeText(PhotoActivity.this, "Font Size :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
        listDataFilling();
        colorsList.setAdapter(new Colors_Adapter(list, PhotoActivity.this));
        colorsList.addOnItemTouchListener(
                new RecyclerItemClickListener(PhotoActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        QuotesModelClass data = list.get(position);
                        title.setTextColor(Color.parseColor(data.getTitle()));
                    }
                })
        );
    }


    private void checkPermission(final Bitmap bitmap, final String title) {
        Dexter.withActivity(PhotoActivity.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(mContext, "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            prepareShareIntent(bitmap, title);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(PhotoActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void prepareShareIntent(Bitmap bmp, String title) {

        Uri bmpUri = getLocalBitmapUri(bmp, title);
        // see previous remote images section
        // Construct share intent as described above based on bitmap
        Intent shareIntent = new Intent();
        //shareIntent.setPackage("com.whatsapp");
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This image has been shared from " + getResources().getString(R.string.app_name) + " , Download it from here https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Choose App where to Share Image"));

    }

    private Uri getLocalBitmapUri(Bitmap bmp, String title) {
        Uri bmpUri = null;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + getResources().getString(R.string.app_name2) + "_share");
        myDir.mkdirs();

        File file = new File(myDir, getResources().getString(R.string.app_name2) + "_" + title + ".png");
        if (file.exists()) {
            //bmpUri = Uri.fromFile(file);
            //bmpUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
            if (file.delete()) {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //bmpUri = Uri.fromFile(file);
                    bmpUri = FileProvider.getUriForFile(PhotoActivity.this, getApplicationContext().getPackageName() + ".provider", file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //bmpUri = Uri.fromFile(file);
                bmpUri = FileProvider.getUriForFile(PhotoActivity.this, getApplicationContext().getPackageName() + ".provider", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bmpUri;
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        //
        Bitmap scaledBitmap = Bitmap.createBitmap(500, 300, Bitmap.Config.ARGB_8888);

        float scaleX = 500 / (float) v.getWidth();
        float scaleY = 300 / (float) v.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        //canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
        //

        return b;
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }


    private void listDataFilling() {
        list.add(new QuotesModelClass("#000000"));
        list.add(new QuotesModelClass("#0C090A"));
        list.add(new QuotesModelClass("#2C3539"));
        list.add(new QuotesModelClass("#2B1B17"));
        list.add(new QuotesModelClass("#34282C"));
        list.add(new QuotesModelClass("#25383C"));
        list.add(new QuotesModelClass("#3B3131"));
        list.add(new QuotesModelClass("#413839"));
        list.add(new QuotesModelClass("#3D3C3A"));
        list.add(new QuotesModelClass("#463E3F"));
        list.add(new QuotesModelClass("#4C4646"));
        list.add(new QuotesModelClass("#504A4B"));
        list.add(new QuotesModelClass("#565051"));
        list.add(new QuotesModelClass("#5C5858"));
        list.add(new QuotesModelClass("#625D5D"));
        list.add(new QuotesModelClass("#666362"));
        list.add(new QuotesModelClass("#6D6968"));
        list.add(new QuotesModelClass("#726E6D"));
        list.add(new QuotesModelClass("#736F6E"));
        list.add(new QuotesModelClass("#837E7C"));
        list.add(new QuotesModelClass("#848482"));
        list.add(new QuotesModelClass("#B6B6B4"));
        list.add(new QuotesModelClass("#D1D0CE"));
        list.add(new QuotesModelClass("#E5E4E2"));
        list.add(new QuotesModelClass("#BCC6CC"));
        list.add(new QuotesModelClass("#98AFC7"));
        list.add(new QuotesModelClass("#6D7B8D"));
        list.add(new QuotesModelClass("#657383"));
        list.add(new QuotesModelClass("#616D7E"));
        list.add(new QuotesModelClass("#646D7E"));
        list.add(new QuotesModelClass("#566D7E"));
        list.add(new QuotesModelClass("#737CA1"));
        list.add(new QuotesModelClass("#4863A0"));
        list.add(new QuotesModelClass("#2B547E"));
        list.add(new QuotesModelClass("#2B3856"));
        list.add(new QuotesModelClass("#151B54"));
        list.add(new QuotesModelClass("#000080"));
        list.add(new QuotesModelClass("#342D7E"));
        list.add(new QuotesModelClass("#15317E"));
        list.add(new QuotesModelClass("#151B8D"));
        list.add(new QuotesModelClass("#0000A0"));
        list.add(new QuotesModelClass("#0020C2"));
        list.add(new QuotesModelClass("#0041C2"));
        list.add(new QuotesModelClass("#2554C7"));
        list.add(new QuotesModelClass("#1569C7"));
        list.add(new QuotesModelClass("#2B60DE"));
        list.add(new QuotesModelClass("#1F45FC"));
        list.add(new QuotesModelClass("#6960EC"));
        list.add(new QuotesModelClass("#736AFF"));
        list.add(new QuotesModelClass("#357EC7"));
        list.add(new QuotesModelClass("#368BC1"));
        list.add(new QuotesModelClass("#488AC7"));
        list.add(new QuotesModelClass("#3090C7"));
        list.add(new QuotesModelClass("#659EC7"));
        list.add(new QuotesModelClass("#87AFC7"));
        list.add(new QuotesModelClass("#95B9C7"));
        list.add(new QuotesModelClass("#728FCE"));
        list.add(new QuotesModelClass("#2B65EC"));
        list.add(new QuotesModelClass("#306EFF"));
        list.add(new QuotesModelClass("#157DEC"));
        list.add(new QuotesModelClass("#1589FF"));
        list.add(new QuotesModelClass("#6495ED"));
        list.add(new QuotesModelClass("#6698FF"));
        list.add(new QuotesModelClass("#38ACEC"));
        list.add(new QuotesModelClass("#56A5EC"));
        list.add(new QuotesModelClass("#5CB3FF"));
        list.add(new QuotesModelClass("#3BB9FF"));
        list.add(new QuotesModelClass("#79BAEC"));
        list.add(new QuotesModelClass("#82CAFA"));
        list.add(new QuotesModelClass("#82CAFF"));
        list.add(new QuotesModelClass("#A0CFEC"));
        list.add(new QuotesModelClass("#B7CEEC"));
        list.add(new QuotesModelClass("#B4CFEC"));
        list.add(new QuotesModelClass("#C2DFFF"));
        list.add(new QuotesModelClass("#C6DEFF"));
        list.add(new QuotesModelClass("#AFDCEC"));
        list.add(new QuotesModelClass("#ADDFFF"));
        list.add(new QuotesModelClass("#BDEDFF"));
        list.add(new QuotesModelClass("#CFECEC"));
        list.add(new QuotesModelClass("#E0FFFF"));
        list.add(new QuotesModelClass("#EBF4FA"));
        list.add(new QuotesModelClass("#F0F8FF"));
        list.add(new QuotesModelClass("#F0FFFF"));
        list.add(new QuotesModelClass("#CCFFFF"));
        list.add(new QuotesModelClass("#93FFE8"));
        list.add(new QuotesModelClass("#9AFEFF"));
        list.add(new QuotesModelClass("#7FFFD4"));
        list.add(new QuotesModelClass("#00FFFF"));
        list.add(new QuotesModelClass("#7DFDFE"));
        list.add(new QuotesModelClass("#57FEFF"));
        list.add(new QuotesModelClass("#8EEBEC"));
        list.add(new QuotesModelClass("#50EBEC"));
        list.add(new QuotesModelClass("#4EE2EC"));
        list.add(new QuotesModelClass("#81D8D0"));
        list.add(new QuotesModelClass("#92C7C7"));
        list.add(new QuotesModelClass("#77BFC7"));
        list.add(new QuotesModelClass("#78C7C7"));
        list.add(new QuotesModelClass("#48CCCD"));
        list.add(new QuotesModelClass("#43C6DB"));
        list.add(new QuotesModelClass("#46C7C7"));
        list.add(new QuotesModelClass("#7BCCB5"));
        list.add(new QuotesModelClass("#43BFC7"));
        list.add(new QuotesModelClass("#3EA99F"));
        list.add(new QuotesModelClass("#3B9C9C"));
        list.add(new QuotesModelClass("#438D80"));
        list.add(new QuotesModelClass("#348781"));
        list.add(new QuotesModelClass("#307D7E"));
        list.add(new QuotesModelClass("#5E7D7E"));
        list.add(new QuotesModelClass("#4C787E"));
        list.add(new QuotesModelClass("#008080"));
        list.add(new QuotesModelClass("#4E8975"));
        list.add(new QuotesModelClass("#78866B"));
        list.add(new QuotesModelClass("#848b79"));
        list.add(new QuotesModelClass("#617C58"));
        list.add(new QuotesModelClass("#728C00"));
        list.add(new QuotesModelClass("#667C26"));
        list.add(new QuotesModelClass("#254117"));
        list.add(new QuotesModelClass("#306754"));
        list.add(new QuotesModelClass("#347235"));
        list.add(new QuotesModelClass("#437C17"));
        list.add(new QuotesModelClass("#387C44"));
        list.add(new QuotesModelClass("#347C2C"));
        list.add(new QuotesModelClass("#347C17"));
        list.add(new QuotesModelClass("#348017"));
        list.add(new QuotesModelClass("#4E9258"));
        list.add(new QuotesModelClass("#6AA121"));
        list.add(new QuotesModelClass("#4AA02C"));
        list.add(new QuotesModelClass("#41A317"));
        list.add(new QuotesModelClass("#3EA055"));
        list.add(new QuotesModelClass("#6CBB3C"));
        list.add(new QuotesModelClass("#6CC417"));
        list.add(new QuotesModelClass("#4CC417"));
        list.add(new QuotesModelClass("#52D017"));
        list.add(new QuotesModelClass("#4CC552"));
        list.add(new QuotesModelClass("#54C571"));
        list.add(new QuotesModelClass("#99C68E"));
        list.add(new QuotesModelClass("#89C35C"));
        list.add(new QuotesModelClass("#85BB65"));
        list.add(new QuotesModelClass("#8BB381"));
        list.add(new QuotesModelClass("#9CB071"));
        list.add(new QuotesModelClass("#B2C248"));
        list.add(new QuotesModelClass("#9DC209"));
        list.add(new QuotesModelClass("#A1C935"));
        list.add(new QuotesModelClass("#7FE817"));
        list.add(new QuotesModelClass("#59E817"));
        list.add(new QuotesModelClass("#57E964"));
        list.add(new QuotesModelClass("#64E986"));
        list.add(new QuotesModelClass("#5EFB6E"));
        list.add(new QuotesModelClass("#00FF00"));
        list.add(new QuotesModelClass("#5FFB17"));
        list.add(new QuotesModelClass("#87F717"));
        list.add(new QuotesModelClass("#8AFB17"));
        list.add(new QuotesModelClass("#6AFB92"));
        list.add(new QuotesModelClass("#98FF98"));
        list.add(new QuotesModelClass("#B5EAAA"));
        list.add(new QuotesModelClass("#C3FDB8"));
        list.add(new QuotesModelClass("#CCFB5D"));
        list.add(new QuotesModelClass("#B1FB17"));
        list.add(new QuotesModelClass("#BCE954"));
        list.add(new QuotesModelClass("#EDDA74"));
        list.add(new QuotesModelClass("#EDE275"));
        list.add(new QuotesModelClass("#FFE87C"));
        list.add(new QuotesModelClass("#FFFF00"));
        list.add(new QuotesModelClass("#FFF380"));
        list.add(new QuotesModelClass("#FFFFC2"));
        list.add(new QuotesModelClass("#FFFFCC"));
        list.add(new QuotesModelClass("#FFF8C6"));
        list.add(new QuotesModelClass("#FFF8DC"));
        list.add(new QuotesModelClass("#F5F5DC"));
        list.add(new QuotesModelClass("#FBF6D9"));
        list.add(new QuotesModelClass("#FAEBD7"));
        list.add(new QuotesModelClass("#F7E7CE"));
        list.add(new QuotesModelClass("#FFEBCD"));
        list.add(new QuotesModelClass("#F3E5AB"));
        list.add(new QuotesModelClass("#ECE5B6"));
        list.add(new QuotesModelClass("#FFE5B4"));
        list.add(new QuotesModelClass("#FFDB58"));
        list.add(new QuotesModelClass("#FFD801"));
        list.add(new QuotesModelClass("#FDD017"));
        list.add(new QuotesModelClass("#EAC117"));
        list.add(new QuotesModelClass("#F2BB66"));
        list.add(new QuotesModelClass("#FBB917"));
        list.add(new QuotesModelClass("#FBB117"));
        list.add(new QuotesModelClass("#FFA62F"));
        list.add(new QuotesModelClass("#E9AB17"));
        list.add(new QuotesModelClass("#E2A76F"));
        list.add(new QuotesModelClass("#DEB887"));
        list.add(new QuotesModelClass("#FFCBA4"));
        list.add(new QuotesModelClass("#C9BE62"));
        list.add(new QuotesModelClass("#E8A317"));
        list.add(new QuotesModelClass("#EE9A4D"));
        list.add(new QuotesModelClass("#C8B560"));
        list.add(new QuotesModelClass("#D4A017"));
        list.add(new QuotesModelClass("#C2B280"));
        list.add(new QuotesModelClass("#C7A317"));
        list.add(new QuotesModelClass("#C68E17"));
        list.add(new QuotesModelClass("#B5A642"));
        list.add(new QuotesModelClass("#ADA96E"));
        list.add(new QuotesModelClass("#C19A6B"));
        list.add(new QuotesModelClass("#CD7F32"));
        list.add(new QuotesModelClass("#C88141"));
        list.add(new QuotesModelClass("#C58917"));
        list.add(new QuotesModelClass("#AF9B60"));
        list.add(new QuotesModelClass("#AF7817"));
        list.add(new QuotesModelClass("#B87333"));
        list.add(new QuotesModelClass("#966F33"));
        list.add(new QuotesModelClass("#806517"));
        list.add(new QuotesModelClass("#827839"));
        list.add(new QuotesModelClass("#827B60"));
        list.add(new QuotesModelClass("#786D5F"));
        list.add(new QuotesModelClass("#493D26"));
        list.add(new QuotesModelClass("#483C32"));
        list.add(new QuotesModelClass("#6F4E37"));
        list.add(new QuotesModelClass("#835C3B"));
        list.add(new QuotesModelClass("#7F5217"));
        list.add(new QuotesModelClass("#7F462C"));
        list.add(new QuotesModelClass("#C47451"));
        list.add(new QuotesModelClass("#C36241"));
        list.add(new QuotesModelClass("#C35817"));
        list.add(new QuotesModelClass("#C85A17"));
        list.add(new QuotesModelClass("#CC6600"));
        list.add(new QuotesModelClass("#E56717"));
        list.add(new QuotesModelClass("#E66C2C"));
        list.add(new QuotesModelClass("#F87217"));
        list.add(new QuotesModelClass("#F87431"));
        list.add(new QuotesModelClass("#E67451"));
        list.add(new QuotesModelClass("#FF8040"));
        list.add(new QuotesModelClass("#F88017"));
        list.add(new QuotesModelClass("#FF7F50"));
        list.add(new QuotesModelClass("#F88158"));
        list.add(new QuotesModelClass("#F9966B"));
        list.add(new QuotesModelClass("#E78A61"));
        list.add(new QuotesModelClass("#E18B6B"));
        list.add(new QuotesModelClass("#E77471"));
        list.add(new QuotesModelClass("#F75D59"));
        list.add(new QuotesModelClass("#E55451"));
        list.add(new QuotesModelClass("#E55B3C"));
        list.add(new QuotesModelClass("#FF0000"));
        list.add(new QuotesModelClass("#FF2400"));
        list.add(new QuotesModelClass("#F62217"));
        list.add(new QuotesModelClass("#F70D1A"));
        list.add(new QuotesModelClass("#F62817"));
        list.add(new QuotesModelClass("#E42217"));
        list.add(new QuotesModelClass("#E41B17"));
        list.add(new QuotesModelClass("#DC381F"));
        list.add(new QuotesModelClass("#C34A2C"));
        list.add(new QuotesModelClass("#C24641"));
        list.add(new QuotesModelClass("#C04000"));
        list.add(new QuotesModelClass("#C11B17"));
        list.add(new QuotesModelClass("#9F000F"));
        list.add(new QuotesModelClass("#990012"));
        list.add(new QuotesModelClass("#8C001A"));
        list.add(new QuotesModelClass("#954535"));
        list.add(new QuotesModelClass("#7E3517"));
        list.add(new QuotesModelClass("#8A4117"));
        list.add(new QuotesModelClass("#7E3817"));
        list.add(new QuotesModelClass("#800517"));
        list.add(new QuotesModelClass("#810541"));
        list.add(new QuotesModelClass("#7D0541"));
        list.add(new QuotesModelClass("#7E354D"));
        list.add(new QuotesModelClass("#7D0552"));
        list.add(new QuotesModelClass("#7F4E52"));
        list.add(new QuotesModelClass("#7F5A58"));
        list.add(new QuotesModelClass("#7F525D"));
        list.add(new QuotesModelClass("#B38481"));
        list.add(new QuotesModelClass("#C5908E"));
        list.add(new QuotesModelClass("#C48189"));
        list.add(new QuotesModelClass("#C48793"));
        list.add(new QuotesModelClass("#E8ADAA"));
        list.add(new QuotesModelClass("#ECC5C0"));
        list.add(new QuotesModelClass("#EDC9AF"));
        list.add(new QuotesModelClass("#FDD7E4"));
        list.add(new QuotesModelClass("#FCDFFF"));
        list.add(new QuotesModelClass("#FFDFDD"));
        list.add(new QuotesModelClass("#FBBBB9"));
        list.add(new QuotesModelClass("#FAAFBE"));
        list.add(new QuotesModelClass("#FAAFBA"));
        list.add(new QuotesModelClass("#F9A7B0"));
        list.add(new QuotesModelClass("#E7A1B0"));
        list.add(new QuotesModelClass("#E799A3"));
        list.add(new QuotesModelClass("#E38AAE"));
        list.add(new QuotesModelClass("#F778A1"));
        list.add(new QuotesModelClass("#E56E94"));
        list.add(new QuotesModelClass("#F660AB"));
        list.add(new QuotesModelClass("#FC6C85"));
        list.add(new QuotesModelClass("#F6358A"));
        list.add(new QuotesModelClass("#F52887"));
        list.add(new QuotesModelClass("#E45E9D"));
        list.add(new QuotesModelClass("#E4287C"));
        list.add(new QuotesModelClass("#F535AA"));
        list.add(new QuotesModelClass("#FF00FF"));
        list.add(new QuotesModelClass("#E3319D"));
        list.add(new QuotesModelClass("#F433FF"));
        list.add(new QuotesModelClass("#D16587"));
        list.add(new QuotesModelClass("#C25A7C"));
        list.add(new QuotesModelClass("#CA226B"));
        list.add(new QuotesModelClass("#C12869"));
        list.add(new QuotesModelClass("#C12267"));
        list.add(new QuotesModelClass("#C25283"));
        list.add(new QuotesModelClass("#C12283"));
        list.add(new QuotesModelClass("#B93B8F"));
        list.add(new QuotesModelClass("#7E587E"));
        list.add(new QuotesModelClass("#571B7E"));
        list.add(new QuotesModelClass("#583759"));
        list.add(new QuotesModelClass("#4B0082"));
        list.add(new QuotesModelClass("#461B7E"));
        list.add(new QuotesModelClass("#4E387E"));
        list.add(new QuotesModelClass("#614051"));
        list.add(new QuotesModelClass("#5E5A80"));
        list.add(new QuotesModelClass("#6A287E"));
        list.add(new QuotesModelClass("#7D1B7E"));
        list.add(new QuotesModelClass("#A74AC7"));
        list.add(new QuotesModelClass("#B048B5"));
        list.add(new QuotesModelClass("#6C2DC7"));
        list.add(new QuotesModelClass("#842DCE"));
        list.add(new QuotesModelClass("#8D38C9"));
        list.add(new QuotesModelClass("#7A5DC7"));
        list.add(new QuotesModelClass("#7F38EC"));
        list.add(new QuotesModelClass("#8E35EF"));
        list.add(new QuotesModelClass("#893BFF"));
        list.add(new QuotesModelClass("#8467D7"));
        list.add(new QuotesModelClass("#A23BEC"));
        list.add(new QuotesModelClass("#B041FF"));
        list.add(new QuotesModelClass("#C45AEC"));
        list.add(new QuotesModelClass("#9172EC"));
        list.add(new QuotesModelClass("#9E7BFF"));
        list.add(new QuotesModelClass("#D462FF"));
        list.add(new QuotesModelClass("#E238EC"));
        list.add(new QuotesModelClass("#C38EC7"));
        list.add(new QuotesModelClass("#C8A2C8"));
        list.add(new QuotesModelClass("#E6A9EC"));
        list.add(new QuotesModelClass("#E0B0FF"));
        list.add(new QuotesModelClass("#C6AEC7"));
        list.add(new QuotesModelClass("#F9B7FF"));
        list.add(new QuotesModelClass("#D2B9D3"));
        list.add(new QuotesModelClass("#E9CFEC"));
        list.add(new QuotesModelClass("#EBDDE2"));
        list.add(new QuotesModelClass("#E3E4FA"));
        list.add(new QuotesModelClass("#FDEEF4"));
        list.add(new QuotesModelClass("#FFF5EE"));
        list.add(new QuotesModelClass("#FEFCFF"));
        list.add(new QuotesModelClass("#FFFFFF"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMG_CHOOSE) {
            if (resultCode == RESULT_OK) {
                imageURI = data.getData();
                chooseImg.setImageURI(imageURI);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }
    }
}