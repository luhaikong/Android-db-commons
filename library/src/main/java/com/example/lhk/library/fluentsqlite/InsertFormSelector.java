package com.example.lhk.library.fluentsqlite;

import com.example.lhk.library.fluentsqlite.Insert.DefaultValuesInsert;

public interface InsertFormSelector extends InsertValuesBuilder, InsertSubqueryForm {
  DefaultValuesInsert defaultValues(String nullColumnHack);
}
