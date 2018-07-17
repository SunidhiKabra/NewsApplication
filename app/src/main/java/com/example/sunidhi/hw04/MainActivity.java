package com.example.sunidhi.hw04;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetDataAsync.HandleData {
    static int count = 0;
    static ProgressDialog progressDialog;
    static int flag=1;
    static ArrayList<Item> items = new ArrayList<>();
    TextView keywordTextView  ;
    ImageView nextImageView ;
    ImageView previousImageView ;
    TextView textViewTotalNumber;
    TextView textViewTitle;
    TextView textViewPublishedAt;
    TextView textViewDescription;
    TextView textViewNumber;
    TextView textViewOutOf;
    ImageView imageView;
    String[] arr = {"Top Stories" , "World", "U.S.", "Business", "Politics", "Technology", "Health", "Entertainment", "Travel", "Living", "Most Recent"};
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageViewNews);
        keywordTextView = findViewById(R.id.keywordTextView);
        nextImageView = findViewById(R.id.nextImageView);
        previousImageView = findViewById(R.id.previousImageView);
        progressDialog = new ProgressDialog(MainActivity.this);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewPublishedAt = findViewById(R.id.textViewPublishedAt);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewNumber = findViewById(R.id.textViewNumber);
        textViewTotalNumber = findViewById(R.id.textViewTotalNumber);
        textViewOutOf = findViewById(R.id.textViewOutOf);
        nextImageView.setEnabled(false);
        previousImageView.setEnabled(false);
        findViewById(R.id.goButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected())
                {
                    MainActivity.items.removeAll(items);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose Category");
                    builder.setItems(arr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            category = arr[i];
                            keywordTextView.setText(arr[i]);

                            switch (category)
                            {
                                case "Top Stories":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_topstories.rss") ;
                                    break;

                                case "World":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_world.rss") ;
                                    break;

                                case "U.S.":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_us.rss") ;
                                    break;

                                case "Business":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/money_latest.rss") ;
                                    break;

                                case "Politics":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_allpolitics.rss") ;
                                    break;

                                case "Technology":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_tech.rss") ;
                                    break;

                                case "Health":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_health.rss") ;
                                    break;

                                case "Entertainment":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_showbiz.rss") ;
                                    break;

                                case "Travel":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_travel.rss") ;
                                    break;

                                case "Living":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_living.rss") ;
                                    break;

                                case "Most Recent":
                                    new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_latest.rss") ;
                                    break;

                                default:
                                    break;

                            }


                        }
                    }).show();
                    count = 0;
                    nextImageView.setEnabled(true);
                    previousImageView.setEnabled(true);
                    if(flag==1)
                    {
                        nextImageView.setEnabled(false);
                        previousImageView.setEnabled(false);
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No internet Access", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;
                ++count;
                if(count <= items.size()-1)
                {
                    textViewOutOf.setVisibility(View.VISIBLE);
                    textViewTitle.setText(items.get(count).getTitle());

                    if(items.get(count).getTitle().matches(""))
                    {
                        textViewTitle.setClickable(false);
                    }
                    else
                    {
                        textViewTitle.setClickable(true);
                        textViewTitle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = items.get(count).getLink();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData( Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    }
                    textViewPublishedAt.setText(items.get(count).getPubDate());
                    Picasso.with(MainActivity.this).load(String.valueOf(items.get(count).getImageUrl())).into(imageView);

                    if(items.get(count).getImageUrl()!=null)
                    {
                        imageView.setClickable(true);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = items.get(count).getLink();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    }
                    else
                    {
                        imageView.setClickable(false);
                    }
                    textViewDescription.setText(items.get(count).getDescription());
                    textViewNumber.setText("" + (String.valueOf(count + 1)));
                    textViewTotalNumber.setText(String.valueOf(items.size()));

                }
                else
                {
                    count=0;
                    textViewOutOf.setVisibility(View.VISIBLE);
                    textViewTitle.setText(items.get(count).getTitle());
                    if(items.get(count).getTitle().matches(""))
                    {
                        textViewTitle.setClickable(false);
                    }
                    else
                    {
                        textViewTitle.setClickable(true);
                        textViewTitle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = items.get(count).getLink();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    }
                    textViewPublishedAt.setText(items.get(count).getPubDate());
                    Picasso.with(MainActivity.this).load(String.valueOf(items.get(count).getImageUrl())).into(imageView);
                    if(items.get(count).getImageUrl()!=null)
                    {
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = MainActivity.items.get(count).getLink();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    }

                    else
                    {
                        imageView.setClickable(false);
                    }
                    textViewDescription.setText(items.get(count).getDescription());
                    textViewNumber.setText("" + (String.valueOf(count + 1)));
                    textViewTotalNumber.setText(String.valueOf(items.size()));
                }
            }
        });

        previousImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;
                --count;
                if(count >= 0)
                {
                    textViewOutOf.setVisibility(View.VISIBLE);
                    textViewTitle.setText(items.get(count).getTitle());

                    if(items.get(count).getTitle().matches(""))
                    {
                        textViewTitle.setClickable(false);
                    }
                    else
                    {
                        textViewTitle.setClickable(true);
                        textViewTitle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = items.get(count).getLink();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    }

                    textViewPublishedAt.setText(items.get(count).getPubDate());
                    Picasso.with(MainActivity.this).load(String.valueOf(items.get(count).getImageUrl())).into(imageView);
                    if(items.get(count).getImageUrl()!=null)
                    {
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = MainActivity.items.get(count).getLink();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    }

                    else
                    {
                        imageView.setClickable(false);
                    }
                    textViewDescription.setText(items.get(count).getDescription());
                    textViewNumber.setText("" + (String.valueOf(count + 1)));
                    textViewTotalNumber.setText(String.valueOf(items.size()));
                }
                else
                {
                    count = items.size() - 1;
                    textViewOutOf.setVisibility(View.VISIBLE);
                    count= items.size()-1;
                    textViewTitle.setText(items.get(count).getTitle());

                    if(items.get(count).getTitle().matches(""))
                    {
                        textViewTitle.setClickable(false);
                    }
                    else
                    {
                        textViewTitle.setClickable(true);
                        textViewTitle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = items.get(count).getLink();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    }

                    textViewPublishedAt.setText(items.get(count).getPubDate());
                    Picasso.with(MainActivity.this).load(String.valueOf(items.get(count).getImageUrl())).into(imageView);
                    if(items.get(count).getImageUrl()!=null)
                    {
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = MainActivity.items.get(count).getLink();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    }

                    else
                    {
                        imageView.setClickable(false);
                    }


                    textViewDescription.setText(items.get(count).getDescription());
                    textViewNumber.setText("" + (String.valueOf(count + 1)));
                    textViewTotalNumber.setText(String.valueOf(items.size()));
                }
            }
        });
    }

    private boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwi = cm.getActiveNetworkInfo();

        if(nwi == null || !nwi.isConnected() ||
                ((nwi.getType() != ConnectivityManager.TYPE_WIFI) &&
                        nwi.getType() != ConnectivityManager.TYPE_MOBILE))
        {
            Toast.makeText(this, "No internet access", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void handledata(ArrayList<Item> items) {
        Log.d("demo","size of list "+ String.valueOf(items.size()));

        MainActivity.progressDialog.dismiss();


        if(items.size() < 2) {
            nextImageView.setEnabled(false);
            previousImageView.setEnabled(false);
            flag=1;
        }
        else{
            nextImageView.setEnabled(true);
            previousImageView.setEnabled(true);
        }
        if(items.size() != 0) {
            flag = 0;
            MainActivity.items = items;
            textViewTitle.setText(MainActivity.items.get(count).getTitle());

            if (MainActivity.items.get(count).getTitle().matches("")) {
                textViewTitle.setClickable(false);
            } else {
                textViewTitle.setClickable(true);
                textViewTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = MainActivity.items.get(count).getLink();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
            }

            textViewOutOf.setVisibility(View.VISIBLE);Picasso.with(MainActivity.this).load(MainActivity.items.get(count).getImageUrl()).into(imageView);
            if(items.get(count).getImageUrl()!=null)
            {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = MainActivity.items.get(count).getLink();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
            }

            else
            {
                imageView.setClickable(false);
            }
            textViewPublishedAt.setText(MainActivity.items.get(count).getPubDate());
            textViewDescription.setText(MainActivity.items.get(count).getDescription());
            textViewNumber.setText("" + (String.valueOf(count + 1)));
            textViewTotalNumber.setText(String.valueOf(MainActivity.items.size()));
        }
        else{
            Toast.makeText(this, "No News Found", Toast.LENGTH_SHORT).show();
            textViewTitle.setText("");
            textViewDescription.setText("");
            textViewNumber.setText("");
            textViewOutOf.setVisibility(View.GONE);
            textViewTotalNumber.setText("");
            textViewPublishedAt.setText("");
            imageView.setImageBitmap(null);

        }
    }
}