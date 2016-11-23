package com.example.lhk.library.provider;

import android.content.ContentProviderOperation;

public interface ConvertibleToOperation {

  ContentProviderOperation.Builder toContentProviderOperationBuilder(UriDecorator uriDecorator);
  ContentProviderOperation toContentProviderOperation(UriDecorator uriDecorator);
}
