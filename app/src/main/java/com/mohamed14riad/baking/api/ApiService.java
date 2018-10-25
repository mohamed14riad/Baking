package com.mohamed14riad.baking.api;

import java.util.ArrayList;

import com.mohamed14riad.baking.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
