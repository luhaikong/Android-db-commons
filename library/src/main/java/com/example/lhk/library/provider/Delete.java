package com.example.lhk.library.provider;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.net.Uri;
import android.os.RemoteException;

import java.util.Collection;

public class Delete extends ProviderAction<Integer> implements ConvertibleToOperation {

  private final Selection selection = new Selection();

  Delete(Uri uri) {
    super(uri);
  }

  @SafeVarargs
  public final <T> Delete where(String selection, T... selectionArgs) {
    this.selection.append(selection, selectionArgs);
    return this;
  }

  public <T> Delete whereIn(String column, Collection<T> collection) {
    this.selection.append(column + " IN (" + Joiner.on(",").join(Collections2.transform(collection, Utils.toEscapedSqlFunction())) + ")");
    return this;
  }

  @Override
  public Integer perform(CrudHandler crudHandler) throws RemoteException {
    return crudHandler.delete(getUri(), selection.getSelection(), selection.getSelectionArgs());
  }

  @Override
  public ContentProviderOperation toContentProviderOperation(UriDecorator uriDecorator) {
    return toContentProviderOperationBuilder(uriDecorator).build();
  }

  @Override
  public Builder toContentProviderOperationBuilder(UriDecorator uriDecorator) {
    return ContentProviderOperation.newDelete(uriDecorator.decorate(getUri()))
        .withSelection(selection.getSelection(), selection.getSelectionArgs());
  }
}
