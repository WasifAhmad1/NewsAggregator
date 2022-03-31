package com.example.newsaggregator;

import static com.example.newsaggregator.MainActivity.opt_subMenu_All;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NewsInfoActivity extends AppCompatActivity {

    /*private TextView aticleTitle;
    private TextView dateTime;
    private TextView articleAuthor;
    private TextView articleText;
    private ImageView articleImage;
    private Picasso picasso; */


    private ViewPager2 viewPager2;
    private NewsAdapter newsAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> arrayAdapter;
    private ConstraintLayout mConstraintLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String DATA_URL =
            "https://newsapi.org/v2/top-headlines?sources=";
    private static final String yourAPIKey =
            "&apiKey=065178ece2124e36a1993080c5d97418";
    private RequestQueue queue;
    private static ArrayList<NewsInfo> newsInfos = new ArrayList<>();

    private static HashMap<String, String> newsTitles = new HashMap<>();
    private static HashMap<String, String> newsLang = new HashMap<>();
    private static HashMap<String, String> newsCountry = new HashMap<>();
    private static HashMap<String, String> newsCategory = new HashMap<>();


    private static boolean langSelect = false;
    private static boolean countSelect = false;
    private static boolean catSelect = false;

    private static boolean allLangSelect = false;
    private static boolean allCountSelect = false;
    private static boolean allCatSelect = false;

    private static ArrayList<News> itemList = new ArrayList<>();

    private static String currentLanguage = "";
    private static String currentCategory = "";
    private static String currentCountry = "";

    private Menu opt_menu;
    private SubMenu langs;
    private SubMenu cats;
    private SubMenu counts;

    private static final String DATA_URL_Initial =
            "https://newsapi.org/v2/sources?";
    private static final String yourAPIKey_Initial =
            "065178ece2124e36a1993080c5d97418";

    private RequestQueue queueInitial;

    private HashMap<String, String> abbrToName = new HashMap<>();
    private HashMap<String, String> abbrToName2 = new HashMap<>();


    private ArrayList<String> newsList = new ArrayList<>();
    private ArrayList<String> catList = new ArrayList<>();
    private ArrayList<String> langList = new ArrayList<>();
    private ArrayList<String> countryList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        viewPager2 = findViewById(R.id.view_pager);
        newsAdapter = new NewsAdapter(this, newsInfos);
        viewPager2.setAdapter(newsAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        queue = Volley.newRequestQueue(this);
        queueInitial = Volley.newRequestQueue(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        mConstraintLayout = findViewById(R.id.c_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }



        String yourTitle = getIntent().getStringExtra("title");
        //now we will use this tring to make a api call. First we need to put a lower case letter
        //in the first char position of the string

        yourTitle = yourTitle.toLowerCase();
        yourTitle = yourTitle.replaceAll("\\s","");
        String finalBoss = DATA_URL.concat(yourTitle.trim()).concat(yourAPIKey.trim());
        String url = finalBoss;
        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        String urlToUse = buildURL.build().toString();


        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    viewPager2.setBackground(null);
                    newsInfos.clear();
                    JSONArray jNews = response.getJSONArray("articles");
                    //System.out.println(jNews.getJSONObject(0).toString());
                    for(int i = 0; i<jNews.length(); i++) {
                        NewsInfo newsInfo = new NewsInfo("", "", "", "",
                        "", "", "");
                        JSONObject jHield = jNews.getJSONObject(i);
                        JSONObject jSource = jHield.getJSONObject("source");
                        String title = jSource.getString("name");
                        newsInfo.setNewsTitle(title);
                        String author = jHield.getString("author");
                        String articleTitle = jHield.getString("title");
                        String url = jHield.getString("url");
                        String imageUrl = jHield.getString("urlToImage");
                        String content = jHield.getString("description");
                        String date = jHield.getString("publishedAt");
                        newsInfo.setAuthor(author);
                        newsInfo.setArticleTitle(articleTitle);
                        newsInfo.setNewsTitle(title);
                        newsInfo.setImageURL(imageUrl);
                        newsInfo.setText(content);
                        newsInfo.setUrl(url);
                        newsInfo.setDate(date);
                        setTitle(newsInfo.getNewsTitle());
                        newsInfos.add(newsInfo);
                    }
                    newsAdapter.notifyItemRangeChanged(0,newsInfos.size());

                System.out.println("break");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("In the error");
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlToUse, null, listener, error)
        {@Override
        public Map<String, String> getHeaders() {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "");
            return headers;
        }
        };
        queue.add(jsonObjectRequest);


        String urlInitial = DATA_URL_Initial;
        Uri.Builder buildURLInitial = Uri.parse(urlInitial).buildUpon();
        buildURLInitial.appendQueryParameter("apiKey", yourAPIKey_Initial);
        String urlToUseInitial = buildURLInitial.build().toString();

        Response.Listener<JSONObject> listenerInitial = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jOfficial = response.getJSONArray("sources");
                    for(int i = 0; i<jOfficial.length(); i++) {
                        JSONObject jHield = jOfficial.getJSONObject(i);
                        String title = jHield.getString("name");
                        String country = jHield.getString("country");
                        String cat = jHield.getString("category");
                        String lang = jHield.getString("language");
                        String id = jHield.getString("id");
                        //idea: build a hashmap containing the id's along with the names of the titles
                        //then build a hashmap consisting of the id's along with the languages, categories
                        //and countries. We can use these hashmaps later to dynamically re-build the menus
                        //again
                        newsList.add(title);
                        catList.add(cat);
                        genCountryLanguage(title, country, lang, cat);
                        newsTitles.put(title, id);
                        newsCategory.put(title, cat);
                    } } catch (JSONException e) {
                    e.printStackTrace();
                }

                addList(newsList);
                genMenu(catList, langList, countryList);
            }
        };

        Response.ErrorListener errorInitial = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("In the error");
            }
        };

        JsonObjectRequest jsonObjectRequestInitial = new JsonObjectRequest
                (Request.Method.GET, urlToUseInitial, null, listenerInitial, errorInitial)
        {@Override
        public Map<String, String> getHeaders() {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "");
            return headers;
        }
        };

        queueInitial.add(jsonObjectRequestInitial);

    }

    public void addList(ArrayList list) {
        arrayAdapter = new ArrayAdapter<>(this,
                R.layout.drawer_entry, list);
        mDrawerList.setAdapter(arrayAdapter);
        mDrawerList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    //in this function we will be able to click on a drawer item and send over a reference
                    // to the NewsInfo activity class in order to generate the ten news articles from the given
                    // publication. The other activity only needs a reference to the title, not anything else
                    //in order to make the api call
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NewsInfoActivity.this.selectItem(position);
                        String daTitle = list.get(position).toString();
                        daTitle = newsTitles.get(daTitle);
                        //viewPager2.setBackground(null);
                        // we need a function of sorts that will take the key and make a new set of
                        //news articles
                        genPager(daTitle);
                        mDrawerLayout.closeDrawer(mConstraintLayout);
                    }
                });

    }

    public void genPager (String key){
        key = key.toLowerCase();
        key = key.replaceAll("\\s","");
        String finalBoss = DATA_URL.concat(key.trim()).concat(yourAPIKey.trim());
        String url = finalBoss;
        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    viewPager2.setBackground(null);
                    newsInfos.clear();
                    JSONArray jNews = response.getJSONArray("articles");
                    //System.out.println(jNews.getJSONObject(0).toString());
                    for(int i = 0; i<jNews.length(); i++) {
                        NewsInfo newsInfo = new NewsInfo("", "", "", "",
                                "", "", "");
                        JSONObject jHield = jNews.getJSONObject(i);
                        JSONObject jSource = jHield.getJSONObject("source");
                        String title = jSource.getString("name");
                        newsInfo.setNewsTitle(title);
                        String author = jHield.getString("author");
                        String articleTitle = jHield.getString("title");
                        String url = jHield.getString("url");
                        String imageUrl = jHield.getString("urlToImage");
                        String content = jHield.getString("description");
                        String date = jHield.getString("publishedAt");
                        newsInfo.setAuthor(author);
                        newsInfo.setArticleTitle(articleTitle);
                        newsInfo.setNewsTitle(title);
                        newsInfo.setImageURL(imageUrl);
                        newsInfo.setText(content);
                        newsInfo.setUrl(url);
                        newsInfo.setDate(date);
                        setTitle(newsInfo.getNewsTitle());
                        newsInfos.add(newsInfo);
                    }
                    newsAdapter.notifyItemRangeChanged(0,newsInfos.size());



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("In the error");
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlToUse, null, listener, error)
        {@Override
        public Map<String, String> getHeaders() {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "");
            return headers;
        }
        };
        queue.add(jsonObjectRequest);



    }

    public void genCountryLanguage (String title, String abb1, String abb2, String category) {
        abb1 = abb1.toUpperCase();
        InputStream inputStream = getResources().openRawResource(R.raw.country_codes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder sb = new StringBuilder();
        try {
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            reader.close();
            inputStream.close();

            JSONObject jo1 = new JSONObject(sb.toString());
            JSONArray ja1 = jo1.getJSONArray("countries");
            for (int i = 0; i < ja1.length(); i++) {
                JSONObject entry = ja1.getJSONObject(i);
                String code = entry.getString("code").trim();
                String name = entry.getString("name").trim();
                abbrToName.put(code, name);

            }
            String country = abbrToName.get(abb1);
            newsCountry.put(title, country);
            if(!countryList.contains(country))
                countryList.add(country);
            System.out.println("break");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        abb2= abb2.toUpperCase();
        InputStream inputStream2 = getResources().openRawResource(R.raw.language_codes);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(inputStream2));

        StringBuilder sb2 = new StringBuilder();
        try {
            String line2 = reader2.readLine();

            while (line2 != null) {
                sb2.append(line2);
                line2 = reader2.readLine();
            }
            reader2.close();
            inputStream2.close();

            JSONObject jo2 = new JSONObject(sb2.toString());
            JSONArray ja2 = jo2.getJSONArray("languages");
            for (int i = 0; i < ja2.length(); i++) {
                JSONObject entry = ja2.getJSONObject(i);
                String code = entry.getString("code").trim();
                String name = entry.getString("name").trim();
                abbrToName2.put(code, name);

            }
            String language = abbrToName2.get(abb2);
            newsLang.put(title, language);
            if(!langList.contains(language))
                langList.add(language);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        News news = new News(title, category, convertCountryCode(abb1), convertLangCode(abb2));
        itemList.add(news);

    }

    public String convertLangCode(String abb) {
        abb = abb.toUpperCase();
        String cool = abbrToName2.get(abb);
        return cool;
    }

    public String convertCountryCode(String abb) {
        abb=abb.toUpperCase();
        String cool = abbrToName.get(abb);
        return cool;
    }

    public void genMenu (ArrayList <String> categories, ArrayList <String> languages,
                         ArrayList <String> countries) {
        Set<String> catSet = new HashSet<>(categories);
        categories.clear();
        categories.addAll(catSet);

        Collections.sort(categories);
        Collections.sort(languages);
        Collections.sort(countries);

        for(String category : categories) {

            cats.add(category);
        }
        for(String langauge : languages){
            langs.add(langauge);
        }

        for(String country : countries) {
            counts.add(country);
        }


    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;

        if(cats == null){
            optSubMenuBuild();
        }

        String key;




        return true;
    }

    private void optSubMenuBuild() {
        if(cats == null){
            cats = opt_menu.addSubMenu("Topics");
            counts = opt_menu.addSubMenu("Countries");
            langs = opt_menu.addSubMenu("Languages");
            cats.add(opt_subMenu_All);
            counts.add(opt_subMenu_All);
            langs.add(opt_subMenu_All);
        }
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Check drawer first!
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        String key;




        executeMenuSelection(item.toString());



        return super.onOptionsItemSelected(item);
    }

    private void executeMenuSelection(String optMenuSelected) {
        String key;
        String pickedTopic = "All";
        String pickedCountry= "All";
        String pickedLanguage = "All";

        boolean checkAllTopics, checkAllCountries, checkAllLanguages;

        switch(optMenuSelected) {
            case "Topics":
                allCatSelect = true;
                allCountSelect = false;
                allLangSelect = false;
                return;

            case "Countries":
                allCountSelect = true;
                allCatSelect = false;
                allLangSelect = false;
                return;

            case "Languages":
                allLangSelect = true;
                allCatSelect = false;
                allCountSelect = false;
                return;
            default:
                if(allCatSelect) pickedTopic = optMenuSelected;
                if(allCountSelect) pickedCountry = optMenuSelected;
                if(allLangSelect) pickedLanguage = optMenuSelected;
                break;
        }
        checkAllTopics = pickedTopic.equals("All");
        checkAllCountries = pickedCountry.equals("All");
        checkAllLanguages = pickedLanguage.equals("All");

        if(optMenuSelected.equals("All") && allCatSelect){
            key = "All";
            topicSelector(key);
        }

        if(optMenuSelected.equals("All") && allLangSelect){
            key = "All";
            languageSelector(key);
        }

        if(optMenuSelected.equals("All") && allCountSelect){
            key = "All";
            countrySelector(key);
        }


        if(newsLang.containsValue(optMenuSelected)){
            key = optMenuSelected;
            languageSelector(key);
        }

        if(newsCountry.containsValue(optMenuSelected)){
            key = optMenuSelected;
            countrySelector(key);
        }


        if(newsCategory.containsValue(optMenuSelected)) {
            key = optMenuSelected;
            topicSelector(key);

        }

    }

    public void topicSelector (String key){
        //we get the name of the topic and then either will get the title or the key
        //if we get the key then we need to run the map

        currentCategory = key;
        ArrayList<String> list = new ArrayList<String>();

        if(!currentCategory.equals("All")) {
            catSelect = true;
            if (langSelect == false && countSelect == false) {
                for (Map.Entry mapElement : newsCategory.entrySet()) {
                    if (mapElement.getValue().toString().equals(key))
                        list.add(mapElement.getKey().toString());
                }
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == true && countSelect == false) {
                for (News item : itemList) {
                    if (key.equals(item.getNewsCategory()) && currentLanguage.equals(item.getNewsLanguage())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == false && countSelect == true) {
                for (News item : itemList) {
                    if (key.equals(item.getNewsCategory()) && currentCountry.equals(item.getNewsCountry())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == true && countSelect == true) {
                for (News item : itemList) {
                    if (key.equals(item.getNewsCategory()) && currentCountry.equals(item.getNewsCountry())
                            && currentLanguage.equals(item.getNewsLanguage())) {
                        list.add(item.getNewsTitle());
                    }
                }

                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();

            }
        }

        else if (currentCategory.equals("All")){
            catSelect=false;
            if (langSelect == false && countSelect == false) {
                for (Map.Entry mapElement : newsCategory.entrySet()) {
                    list.add(mapElement.getKey().toString());
                }
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == true && countSelect == false) {
                for (News item : itemList) {
                    if (currentLanguage.equals(item.getNewsLanguage())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == false && countSelect == true) {
                for (News item : itemList) {
                    if (currentCountry.equals(item.getNewsCountry())) {
                        list.add(item.getNewsTitle());
                    }
                }
                System.out.println("Are we in here");
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == true && countSelect == true) {
                for (News item : itemList) {
                    if (currentCountry.equals(item.getNewsCountry())
                            && currentLanguage.equals(item.getNewsLanguage())) {
                        list.add(item.getNewsTitle());
                    }
                }

                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();

            }

        }
        if(newsList.size()==0){

        }

        arrayAdapter.notifyDataSetChanged();

    }

    public void languageSelector (String key){
        currentLanguage = key;
        ArrayList<String> list = new ArrayList<String>();

        if(!currentLanguage.equals("All")) {
            langSelect = true;
            if (catSelect == false && countSelect == false) {
                for (Map.Entry mapElement : newsLang.entrySet()) {
                    if (mapElement.getValue().toString().equals(key))
                        list.add(mapElement.getKey().toString());
                }
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (catSelect == true && countSelect == false) {
                for (News item : itemList) {
                    if (key.equals(item.getNewsLanguage()) &&
                            currentCategory.equals(item.getNewsCategory())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (catSelect == false && countSelect == true) {
                for (News item : itemList) {
                    if (key.equals(item.getNewsLanguage()) &&
                            currentCountry.equals(item.getNewsCountry())) {
                        list.add(item.getNewsTitle());
                    }
                }

                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (catSelect == true && countSelect == true) {
                for (News item : itemList) {
                    if (key.equals(item.getNewsLanguage()) &&
                            currentCategory.equals(item.getNewsCategory())
                            && currentCountry.equals(item.getNewsCountry())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();

            }
        }

        else if (currentLanguage.equals("All")){
            langSelect = false;
            if (catSelect == false && countSelect == false) {
                for (Map.Entry mapElement : newsLang.entrySet()) {
                    list.add(mapElement.getKey().toString());
                }
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (catSelect == true && countSelect == false) {
                for (News item : itemList) {
                    if (currentCategory.equals(item.getNewsCategory())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (catSelect == false && countSelect == true) {
                for (News item : itemList) {
                    if (currentCountry.equals(item.getNewsCountry())) {
                        list.add(item.getNewsTitle());
                    }
                }

                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (catSelect == true && countSelect == true) {
                for (News item : itemList) {
                    if (currentCategory.equals(item.getNewsCategory())
                            && currentCountry.equals(item.getNewsCountry())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();

            }

        }

        if(newsList.size()==0){

        }
        arrayAdapter.notifyDataSetChanged();



    }

    public void countrySelector (String key){
        currentCountry = key;
        ArrayList<String> list = new ArrayList<String>();

        if(!currentCountry.equals("All")) {
            countSelect = true;
            if (langSelect == false && catSelect == false) {
                for (Map.Entry mapElement : newsCountry.entrySet()) {
                    if (mapElement.getValue().toString().equals(key))
                        list.add(mapElement.getKey().toString());
                }
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == true && catSelect == false) {
                for (News item : itemList) {
                    if (key.equals(item.getNewsCountry()) && currentLanguage.equals(item.getNewsLanguage())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == false && catSelect == true) {
                for (News item : itemList) {
                    if (key.equals(item.getNewsCountry()) && currentCategory.equals(item.getNewsCategory())) {
                        list.add(item.getNewsTitle());
                    }
                }

                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();


            }

            if (langSelect == true && catSelect == true) {
                for (News item : itemList) {
                    if (key.equals(item.getNewsCountry()) && currentCategory.equals(item.getNewsCategory())
                            && currentLanguage.equals(item.getNewsLanguage())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }
        }

        else if (currentCountry.equals("All")) {
            countSelect = false;
            if (langSelect == false && catSelect == false) {
                for (Map.Entry mapElement : newsCountry.entrySet()) {
                    list.add(mapElement.getKey().toString());
                }
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == true && catSelect == false) {
                for (News item : itemList) {
                    if (currentLanguage.equals(item.getNewsLanguage())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }

            if (langSelect == false && catSelect == true) {
                for (News item : itemList) {
                    if (currentCategory.equals(item.getNewsCategory())) {
                        list.add(item.getNewsTitle());
                    }
                }

                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();


            }

            if (langSelect == true && catSelect == true) {
                for (News item : itemList) {
                    if (currentCategory.equals(item.getNewsCategory())
                            && currentLanguage.equals(item.getNewsLanguage())) {
                        list.add(item.getNewsTitle());
                    }
                }
                Set<String> catSet = new HashSet<>(list);
                list.clear();
                list.addAll(catSet);
                newsList.clear();
                newsList.addAll(list);
                setTitle("News Gateway (" + newsList.size() + ")");
                list.clear();
            }


        }

        if(newsList.size()==0){

        }
        arrayAdapter.notifyDataSetChanged();


    }

    private void selectItem(int position) {
        mDrawerLayout.closeDrawer(findViewById(R.id.c_layout));
    }
}