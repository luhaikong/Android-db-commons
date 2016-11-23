package com.example.lhk.library.loaders;

import com.example.lhk.library.common.QueryData;
import com.example.lhk.library.cursors.Cursors;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.Loader;

import java.util.List;

public class TransformedRowLoaderBuilder<T> {

  private final QueryData queryData;
  private final ImmutableList<Uri> notificationUris;
  private final Function<Cursor, T> cursorTransformation;

  public TransformedRowLoaderBuilder(QueryData queryData, ImmutableList<Uri> notificationUris, Function<Cursor, T> transformation) {
    this.queryData = queryData;
    this.notificationUris = notificationUris;
    this.cursorTransformation = transformation;
  }

  public <Out> TransformedRowLoaderBuilder<Out> transformRow(final Function<T, Out> rowTransformer) {
    return new TransformedRowLoaderBuilder<>(queryData, notificationUris, Functions.compose(rowTransformer, cursorTransformation));
  }

  public TransformedLoaderBuilder<List<T>> lazy() {
    return new TransformedLoaderBuilder<>(queryData, notificationUris, getLazyTransformationFunction());
  }

  public <Out> TransformedLoaderBuilder<Out> transform(final Function<List<T>, Out> transformer) {
    return new TransformedLoaderBuilder<>(queryData, notificationUris, Functions.compose(transformer, getEagerTransformationFunction())
    );
  }

  public TransformedRowLoaderBuilder<T> addNotificationUri(Uri uri) {
    return new TransformedRowLoaderBuilder<>(queryData, ImmutableList.<Uri>builder().addAll(notificationUris).add(uri).build(), cursorTransformation);
  }

  public Loader<List<T>> build(Context context) {
    return new ComposedCursorLoader<>(context, queryData, ImmutableList.copyOf(notificationUris), getEagerTransformationFunction());
  }

  private Function<Cursor, List<T>> getEagerTransformationFunction() {
    return new Function<Cursor, List<T>>() {
      @Override
      public List<T> apply(Cursor input) {
        return Lists.newArrayList(Cursors.toFluentIterable(input, cursorTransformation));
      }
    };
  }

  private Function<Cursor, List<T>> getLazyTransformationFunction() {
    return new Function<Cursor, List<T>>() {
      @Override
      public List<T> apply(Cursor cursor) {
        return new LazyCursorList<>(cursor, cursorTransformation);
      }
    };
  }
}
