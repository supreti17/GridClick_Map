package com.surajapps.gridclick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerVAdapter recyclerVAdapter = new RecyclerVAdapter(getApplicationContext(), getBooks());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerVAdapter);
    }

    public ArrayList<Book> getBooks() {
        Book book1 = new Book("To Kill a Mockingbird", "Harper Lee");
        Book book2 = new Book("The Catcher in the Rye", "J. D. Salinger");
        Book book3 = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        Book book4 = new Book("Twilight", "Stephenie Meyer");
        Book book5 = new Book("The Hunger Games", "Suzanne Collins");
        Book book6 = new Book("The Kite Runner", "Khaled Hosseini");
        Book book7 = new Book("Animal Farm", "George Orwell");
        Book book8 = new Book("Jane Eyre", "Charlotte Brontë");
        Book book9 = new Book("Brave New World", "Aldous Huxley");
        Book book10 = new Book("The Lord of the Rings", "J. R. R. Tolkien");
        Book book11 = new Book("New Moon", "Stephenie Meyer");
        Book book12 = new Book("Angels & Demons", "Dan Brown");
        Book book13 = new Book("The Fellowship of the Ring", "J. R. R. Tolkien");
        Book book14 = new Book("Fahrenheit 451", "Ray Bradbury");
        Book book15 = new Book("Wuthering Heights", "Emily Brontë");

        ArrayList<Book> booksList = new ArrayList<>();
        booksList.add(book1);
        booksList.add(book2);
        booksList.add(book3);
        booksList.add(book4);
        booksList.add(book5);
        booksList.add(book6);
        booksList.add(book7);
        booksList.add(book8);
        booksList.add(book9);
        booksList.add(book10);
        booksList.add(book11);
        booksList.add(book12);
        booksList.add(book13);
        booksList.add(book14);
        booksList.add(book15);

        return booksList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about_activity:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
