package com.example.lhk.library.fluentsqlite;

import com.example.lhk.library.fluentsqlite.Insert.InsertWithSelect;
import com.example.lhk.library.fluentsqlite.Query.QueryBuilder;

public interface InsertSubqueryForm {
  InsertSubqueryForm columns(String... columns);
  InsertWithSelect resultOf(Query query);
  InsertWithSelect resultOf(QueryBuilder queryBuilder);
}
