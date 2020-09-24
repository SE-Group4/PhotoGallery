package com.example.photogallery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchUnitTest {
    @Test
    public void dateIsCorrect() {
        assertEquals(true, SearchHelper.searchByDate("2020:05:06", "2020-03-01", "2020-09-01"));
    }

    @Test
    public void dateIsNotCorrect() {
        assertEquals(false, SearchHelper.searchByDate("2020:12:06", "2020-03-01", "2020-09-01"));
    }

    @Test
    public void keywordIsCorrect() {
        String[] dictionary = {"hello", "goodbye"};
        assertEquals(true, SearchHelper.searchByKeyword(dictionary, "hello"));
    }

    @Test
    public void keywordIsInCorrect() {
        String[] dictionary = {"hello", "goodbye"};
        assertEquals(false, SearchHelper.searchByKeyword(dictionary, "not_hello"));
    }
}