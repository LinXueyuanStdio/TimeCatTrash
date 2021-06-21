package com.timecat.component.data.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timecat.component.setting.DEF;
import com.timecat.component.data.model.api.SearchEngine;

import java.util.ArrayList;


public class SearchEngineUtil {
    private static final String SEARCH_ENGINES = "search_engines_list";
    private static final String ENGINES = "[{\n"
            + "\"title\":\"神马搜索\",\n"
            + "\"url\":\"https://m.sm.cn/s?q=\"\n"
            + "},{\n"
            + "\"title\":\"百度\",\n"
            + "\"url\":\"https://m.baidu.com/s?word=\"\n"
            + "},{\n"
            + "\"title\":\"谷歌\",\n"
            + "\"url\":\"https://www.google.com/search?q=\"\n"
            + "},{\n"
            + "\"title\":\"必应\",\n"
            + "\"url\":\"https://www.bing.com/search?q=\"\n"
            + "},{\n"
            + "\"title\":\"淘宝\",\n"
            + "\"url\":\"https://s.m.taobao.com/h5?q=\"\n"
            + "},{\n"
            + "\"title\":\"知乎\",\n"
            + "\"url\":\"https://www.zhihu.com/search?q=\"\n"
            + "}\n"
            + ",{\n"
            + "\"title\":\"谷歌翻译\",\n"
            + "\"url\":\"http://translate.google.cn/m/translate?q=\"\n"
            + "}\n"
            + "]";
    private static SearchEngineUtil searchEngineUtil;
    private ArrayList<SearchEngine> searchEngines;

    public static SearchEngineUtil getInstance() {
        if (searchEngineUtil == null) {
            searchEngineUtil = new SearchEngineUtil();
            if (searchEngineUtil.searchEngines == null) {
                String s = DEF.config().getString(SEARCH_ENGINES, ENGINES);
                searchEngineUtil.searchEngines = new Gson().fromJson(s, new TypeToken<ArrayList<SearchEngine>>() {
                }.getType());
            }
        }
        return searchEngineUtil;
    }

    public void save(ArrayList<SearchEngine> searchEngines) {
        if (searchEngines != null) {
            DEF.config().save(SEARCH_ENGINES, new Gson().toJson(searchEngines));
            searchEngineUtil.searchEngines = searchEngines;
        }
    }

    public ArrayList<String> getSearchEngineNames() {
        ArrayList<String> names = new ArrayList<>();
        for (SearchEngine s : searchEngineUtil.getSearchEngines()) {
            names.add(s.title);
        }
        return names;
    }

    public ArrayList<SearchEngine> getSearchEngines() {
        return searchEngines;
    }

    public void addSearchEngine(SearchEngine searchEngine) {
        if (searchEngines != null) {
            searchEngines.add(searchEngine);
        }
    }
}
