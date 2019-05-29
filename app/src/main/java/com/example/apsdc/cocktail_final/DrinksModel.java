package com.example.apsdc.cocktail_final;

import android.os.Parcel;
import android.os.Parcelable;

class DrinksModel implements Parcelable {
    String id;
    String drinkImage;
    String drinkTitle;
    public DrinksModel(String ids, String imagePoster, String drinkTitle) {
        id=ids;
        drinkImage=imagePoster;
        this.drinkTitle=drinkTitle;
    }

    protected DrinksModel(Parcel in) {
        id = in.readString();
        drinkImage = in.readString();
        drinkTitle = in.readString();
    }

    public static final Creator<DrinksModel> CREATOR = new Creator<DrinksModel>() {
        @Override
        public DrinksModel createFromParcel(Parcel in) {
            return new DrinksModel(in);
        }

        @Override
        public DrinksModel[] newArray(int size) {
            return new DrinksModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrinkImage() {
        return drinkImage;
    }

    public void setDrinkImage(String drinkImage) {
        this.drinkImage = drinkImage;
    }

    public String getDrinkTitle() {
        return drinkTitle;
    }

    public void setDrinkTitle(String drinkTitle) {
        this.drinkTitle = drinkTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(drinkImage);
        dest.writeString(drinkTitle);
    }
}
