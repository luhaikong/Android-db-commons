package com.example.lhk.library.fluentsqlite;

import android.content.ContentValues;

public interface InsertValuesBuilder {
  Insert values(ContentValues values);
  Insert value(String column, Object value);
}
