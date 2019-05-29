package com.example.apsdc.cocktail_final;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    List<DrinksModel> drinksModels;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String ALCOHOLIC = "Alcoholic";
    public static final String ALOCOHOLFREE = "Non_Alcoholic";
    public static final String SHARED_KEY = "key";
    String emp="Alcoholic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_id);
        if (internetConnected()) {
            sharedPreferences = getPreferences(MODE_PRIVATE);

            if (savedInstanceState != null) {
                drinksModels = savedInstanceState.getParcelableArrayList(SHARED_KEY);
                DrinksAdapter myDrinksAdapter = new DrinksAdapter(this, drinksModels);

                int rotation = this.getResources().getConfiguration().orientation;
                if (rotation == Configuration.ORIENTATION_PORTRAIT) {
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
                }
                recyclerView.setAdapter(myDrinksAdapter);
            } else {
                getSupportLoaderManager().destroyLoader(1);
                getSupportLoaderManager().initLoader(1, null, this);
            }
        } else {
            noInternetConnection();
        }

    }

    public boolean internetConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void noInternetConnection() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("no network");
        alertBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                String Urll ="https://www.thecocktaildb.com/api/json/v1/1/filter.php?a="+emp;

                                try {
                    URL url = new URL(Urll);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStreamReader = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamReader));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + "  ");
                    }
                    return stringBuilder.toString();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if (s == null) {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
        }else {
            drinksModels = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("drinks");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1object = jsonArray.getJSONObject(i);
                    String ids = jsonObject1object.getString("idDrink");
                    String imagePoster = jsonObject1object.getString("strDrinkThumb");
                    String drinkTitle = jsonObject1object.getString("strDrink");
                    DrinksModel drinksModel = new DrinksModel(ids, imagePoster, drinkTitle);
                    drinksModels.add(drinksModel);

                }
                DrinksAdapter drinksAdapter = new DrinksAdapter(this,drinksModels);
                int or = getResources().getConfiguration().orientation;
                if (or == Configuration.ORIENTATION_PORTRAIT) {
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

                }
                recyclerView.setAdapter(drinksAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alcoholic:
                editor = sharedPreferences.edit();
                editor.putString(SHARED_KEY, ALCOHOLIC);
                editor.apply();
               emp="Alcoholic";
                getSupportLoaderManager().destroyLoader(1);
                getSupportLoaderManager().initLoader(1, null, this);
                break;
            case R.id.alcoholFree:
                editor = sharedPreferences.edit();
                editor.putString(SHARED_KEY, ALOCOHOLFREE);
                editor.apply();
                emp="Non_Alcoholic";
                getSupportLoaderManager().destroyLoader(2);
                getSupportLoaderManager().initLoader(2, null, this);


        break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SHARED_KEY, (ArrayList<? extends Parcelable>) drinksModels);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(1);
    }
}