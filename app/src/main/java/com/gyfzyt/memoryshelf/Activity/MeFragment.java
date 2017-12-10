package com.gyfzyt.memoryshelf.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gyfzyt.memoryshelf.Beans.bookBean.Book;
import com.gyfzyt.memoryshelf.Beans.movieBean.Movie;
import com.gyfzyt.memoryshelf.DB.MyDBHelper;
import com.gyfzyt.memoryshelf.Dao.BookDBUtil;
import com.gyfzyt.memoryshelf.Dao.MovieDBUtil;
import com.gyfzyt.memoryshelf.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Administrator on 2017/10/14.
 * 用户资料展示界面
 */

public class MeFragment extends android.support.v4.app.Fragment
{
    private TextView readPagesText;//阅读总页数
    private TextView readMoneyText;//阅读总花费
    private TextView watchMovieText;//观影总数
    private TextView timeText;//偏爱的年代

    private MyDBHelper dbHelper;
    private List<Book> bookList;
    private List<Movie> movieList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.my_frag,container,false);

        readPagesText = (TextView) view.findViewById(R.id.read_pages_text);
        readMoneyText = (TextView) view.findViewById(R.id.read_money_text);
        watchMovieText = (TextView) view.findViewById(R.id.watch_movie_text);
        timeText = (TextView) view.findViewById(R.id.favorite_time_text);

        //从本地数据库中获取数据
        dbHelper = new MyDBHelper(getContext(), "shelfDB.db", null, 1);
        Log.i("tag", getContext().toString());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        bookList = BookDBUtil.searchForAll(db);
        db = dbHelper.getReadableDatabase();
        movieList = MovieDBUtil.searchForAll(db);

        readPagesText.setText("阅读总数：\n     " + computeReadPages(bookList)+"页");
        readMoneyText.setText("阅读花费：\n    " + computeReadMoney(bookList));
        watchMovieText.setText("观影总数：\n     " +movieList.size());
        if(bookList.size() == 0 && movieList.size() == 0){

        }else {
            int time =  computeTime(bookList, movieList);
            timeText.setText("偏爱的年代：\n" + (time/10+1)+"世纪"+time%10+"0年代");
        }

        return view;

    }

    /**
     * 计算阅读总数
     * @param bookList bookList
     * @return pages
     */
    public int computeReadPages(List<Book> bookList){
        int pages = 0;
        for (Book book:bookList){
            pages += Integer.valueOf(book.getPages());
        }
        return pages;
    }
    /**
     * 计算阅读花费
     * @param bookList bookList
     * @return money
     */
    public float computeReadMoney(List<Book> bookList){
        float money = 0;
        String s;
        for (Book book:bookList){
            s = book.getPrice();
            money += Float.valueOf(s.substring(0, s.length()-2));
        }
        return money;
    }

    /**
     * 通过用户观看的书籍和电影发行年份，计算用户偏爱年代（1920-2020）
     * @param bookList
     * @param movieList
     * @return 年代（例：198 表示20世纪80年代）
     */
    public int computeTime(List<Book> bookList, List<Movie> movieList){
        List<Integer> timeList = new ArrayList<>();
        String s;
        int[] times = new int[10];

        for (Book book:bookList){
            s = book.getPubdate().substring(0, 4);
            timeList.add(Integer.valueOf(s));
        }
        for (Movie movie : movieList){
            s = movie.getYear().substring(0, 4);
            timeList.add(Integer.valueOf(s));
        }
        for(Integer time : timeList){
            if(time>=1920 && time<1930){
                times[0]++;
            }else if(time >= 1930 && time < 1940){
                times[1]++;
            }else if(time >= 1940 && time < 1950){
                times[2]++;
            }else if(time >= 1950 && time < 1960){
                times[3]++;
            }else if(time >= 1960 && time < 1970){
                times[4]++;
            }else if(time >= 1970 && time < 1980){
                times[5]++;
            }else if(time >= 1980 && time < 1990){
                times[6]++;
            }else if(time >= 1990 && time < 2000){
                times[7]++;
            }else if(time >= 2000 && time < 2010){
                times[8]++;
            }else if(time >= 2010 && time < 2020){
                times[9]++;
            }
        }
        int max = times[0];
        int index = 0;
        for (int i = 1; i < 10; i++){
            if(max < times[i]){
                index = i;
                max = times[i];
            }
        }
        return 192+index;
    }

}
