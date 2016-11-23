package com.example.lhk.android_db_commons.ui;

import com.example.lhk.android_db_commons.content.Contract;
import com.example.lhk.android_db_commons.model.Person;
import com.example.lhk.library.loaders.CursorLoaderBuilder;
import com.example.lhk.library.loaders.LoaderHelper;
import com.google.common.base.Function;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.Loader;
import android.view.View;

import java.util.List;

public class WrapperTransformationListFragment extends ListFragment implements LoaderHelper.LoaderDataCallbacks<SectionedPeopleList> {

  private static final int LOADER_PEOPLE = 0;

  private SectionedPeopleAdapter adapter;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    adapter = new SectionedPeopleAdapter(getActivity());
    setListAdapter(adapter);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    loaderHelper.initLoader(this, null, this);
  }

  private static final LoaderHelper<SectionedPeopleList> loaderHelper = new LoaderHelper<SectionedPeopleList>(LOADER_PEOPLE) {
    @Override
    protected Loader<SectionedPeopleList> onCreateLoader(Context context, Bundle args) {
      return CursorLoaderBuilder.forUri(Contract.People.CONTENT_URI)
          .projection(Contract.People.FIRST_NAME, Contract.People.SECOND_NAME)
          .orderBy(Contract.People.FIRST_NAME)
          .transformRow(new Function<Cursor, Person>() {
            @Override
            public Person apply(Cursor cursor) {
              final String firstName = cursor.getString(cursor.getColumnIndexOrThrow(Contract.PeopleColumns.FIRST_NAME));
              final String secondName = cursor.getString(cursor.getColumnIndexOrThrow(Contract.PeopleColumns.SECOND_NAME));
              return new Person(firstName, secondName);
            }
          })
          .lazy()
          .transform(new Function<List<Person>, SectionedPeopleList>() {
            @Override
            public SectionedPeopleList apply(List<Person> people) {
              return new SectionedPeopleList(people);
            }
          })
          .build(context);
    }
  };

  @Override
  public void onLoadFinished(Loader<SectionedPeopleList> loader, SectionedPeopleList data) {
    adapter.setNewModel(data);
  }

  @Override
  public void onLoaderReset(Loader<SectionedPeopleList> loader) {
    adapter.setNewModel(null);
  }
}
