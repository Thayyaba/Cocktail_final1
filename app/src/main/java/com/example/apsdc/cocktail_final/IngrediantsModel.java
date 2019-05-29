package com.example.apsdc.cocktail_final;

import android.os.Parcel;
import android.os.Parcelable;

class IngrediantsModel implements Parcelable {
    String catogery;
    String type;
    String glasstype;
    String ingrediants;

    public IngrediantsModel(String catogery, String type, String glasstype,String ingrediants) {
        this.catogery = catogery;
        this.type = type;
        this.glasstype = glasstype;
        this.ingrediants=ingrediants;
    }

    public String getIngrediants() {
        return ingrediants;
    }

    public void setIngrediants(String ingrediants) {
        this.ingrediants = ingrediants;
    }

    protected IngrediantsModel(Parcel in) {
        catogery = in.readString();
        type = in.readString();
        glasstype = in.readString();
    }

    public static final Creator<IngrediantsModel> CREATOR = new Creator<IngrediantsModel>() {
        @Override
        public IngrediantsModel createFromParcel(Parcel in) {
            return new IngrediantsModel(in);
        }

        @Override
        public IngrediantsModel[] newArray(int size) {
            return new IngrediantsModel[size];
        }
    };

    public String getCatogery() {
        return catogery;
    }

    public void setCatogery(String catogery) {
        this.catogery = catogery;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGlasstype() {
        return glasstype;
    }

    public void setGlasstype(String glasstype) {
        this.glasstype = glasstype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(catogery);
        dest.writeString(type);
        dest.writeString(glasstype);
    }
}
