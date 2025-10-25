package com.shruti.lofo.ui.DashBoard;

import androidx.lifecycle.ViewModel;

public class DashBoardViewModel extends ViewModel {
    String imageURI;
    String category, description, ownerName, finderName,tag,dateLost,dateFound,itemName;
    String collectionName; // New field to store the collection name

    public DashBoardViewModel(String imageURI, String category, String description, String ownerName, String finderName, String tag,String dateLost,String itemName,String dateFound) {
        this.imageURI = imageURI;
        this.category = category;
        this.description = description;
        this.ownerName = ownerName;
        this.finderName = finderName;
        this.itemName = itemName;
        this.tag = tag;
        this.dateLost = dateLost;
        this.dateFound = dateFound;
    }

    // A method to determine the tag based on the collection name
//    private String determineTag(String collectionName) {
//        if ("lostItems".equals(collectionName)) {
//            return "lost";
//        } else if ("foundItems".equals(collectionName)) {
//            return "found";
//        } else {
//            return ""; // Handle other cases as needed
//        }
//    }
    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setFinderName(String finderName) {
        this.finderName = finderName;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDateLost(String dateLost) {
        this.dateLost = dateLost;
    }

    public void setDateFound(String dateFound) {
        this.dateFound = dateFound;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageURI() {
        return imageURI;
    }
    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public String getFinderName() {
        return finderName;
    }
    public String getDateLost() {
        return dateLost;
    }
    public String getDateFound() {return dateFound;}
    public String getTag() {return tag;}
    public String getItemName(){ return itemName;}

    public DashBoardViewModel() {

//        mText = new MutableLiveData<>();
//        mText.setValue("This is DashBoard fragment");
    }

}