package com.example.apsdc.cocktail_final;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class ActivityDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    ImageView detailsImage;
    TextView id, drinksname, category, gtype, dtype, ins, ing, measure;
    String drinkId;
    List<DrinksModel> drinksModels;
    ArrayList<IngrediantsModel> ingrediantsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailsImage = findViewById(R.id.detail_image);
        id = findViewById(R.id.drink_id);
        drinksname = findViewById(R.id.drink_title);
        category = findViewById(R.id.detail_catogory);
        gtype = findViewById(R.id.detail_glasstype);
        dtype = findViewById(R.id.detail_type);
        ins = findViewById(R.id.details_instructions);
        ing = findViewById(R.id.ingrediants);
        measure = findViewById(R.id.measures);
        String drinkName = getIntent().getStringExtra("DrinkTitle");
        drinkId = getIntent().getStringExtra("DrinkId");
        String pImage = getIntent().getStringExtra("DrinkImage");
        drinksModels = getIntent().getParcelableArrayListExtra("key");
        Picasso.with(this).load(pImage).into(detailsImage);
        drinksname.setText(drinkName);
        id.setText(drinkId);
        getSupportLoaderManager().initLoader(39, null, this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {
            String uRL = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + drinkId;

            @Nullable
            @Override
            public String loadInBackground() {
                try {
                    URL url = new URL(uRL);
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
        } else {
            ingrediantsModels = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("drinks");
                List<String> ingre = new ArrayList<>();
                List<String> meas = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1object = jsonArray.getJSONObject(i);
                    String catogery = jsonObject1object.getString("strCategory");
                    String type = jsonObject1object.getString("strAlcoholic");
                    String glasstype = jsonObject1object.getString("strGlass");
                    String instructions = jsonObject1object.getString("strInstructions");
                    for (int k = 1; k <= 15; k++) {
                        String measure = jsonObject1object.optString("strMeasure" + k);
                        Log.i("mylog",measure);
                        if (!measure.isEmpty() && !measure.equals(" ") && !measure.equals("null")) {
                            meas.add(measure);
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    for (String s2 : meas) {
                        sb.append(s2+"\n");
                    }
                    for (int k = 1; k <= 15; k++) {
                        String ingred = jsonObject1object.getString("strIngredient" + k);
                        Log.i("mylog",ingred);
                        if (!TextUtils.isEmpty(ingred) && !ingred.equals(" ") && !ingred.equals("null")) {
                            ingre.add(ingred);
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s1 : ingre) {
                        stringBuilder.append(s1 + "\n");
                    }
                    measure.setText(sb.toString().trim());
                    ing.setText(stringBuilder.toString().trim());
                    category.setText(catogery);
                    gtype.setText(glasstype);
                    dtype.setText(type);
                    ins.setText(instructions);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

}
