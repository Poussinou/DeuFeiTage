package de.micmun.android.deufeitage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import de.micmun.android.deufeitage.util.StateItem;

/**
 * A list fragment representing a list of FeiTage. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link FeiTagDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class FeiTagListFragment extends ListFragment {

   /**
    * The serialization (saved instance state) Bundle key representing the
    * activated item position. Only used on tablets.
    */
   private static final String STATE_ACTIVATED_POSITION = "activated_position";

   /**
    * The fragment's current callback object, which is notified of list item
    * clicks.
    */
   private Callbacks mCallbacks = sDummyCallbacks;

   /**
    * The current activated item position. Only used on tablets.
    */
   private int mActivatedPosition = ListView.INVALID_POSITION;

   /**
    * A callback interface that all activities containing this fragment must
    * implement. This mechanism allows activities to be notified of item
    * selections.
    */
   public interface Callbacks {
      /**
       * Callback for when an item has been selected.
       *
       * @param id ID of the selected item.
       */
      public void onItemSelected(String id);
   }

   /**
    * A dummy implementation of the {@link Callbacks} interface that does
    * nothing. Used only when this fragment is not attached to an activity.
    */
   private static Callbacks sDummyCallbacks = new Callbacks() {
      @Override
      public void onItemSelected(String id) {
      }
   };

   /**
    * Mandatory empty constructor for the fragment manager to instantiate the
    * fragment (e.g. upon screen orientation changes).
    */
   public FeiTagListFragment() {
   }

   /**
    * @see android.support.v4.app.ListFragment#onCreate(android.os.Bundle)
    */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // create state items
      String[] states = getResources().getStringArray(R.array
            .states_of_germany);
      ArrayList<StateItem> objects = new ArrayList<>(states.length);

      for (int i = 0; i < states.length; ++i) {
         objects.add(new StateItem(StateItem.ITEM_IDS[i], states[i]));
      }

      // set list adapter
      setListAdapter(new StateArrayAdapter(getActivity(), objects));
   }

   /**
    * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
    */
   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      // Restore the previously serialized activated item position.
      if (savedInstanceState != null
            && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
         setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
      }
   }

   /**
    * @see android.support.v4.app.ListFragment#onAttach(android.app.Activity)
    */
   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);

      // Activities containing this fragment must implement its callbacks.
      if (!(activity instanceof Callbacks)) {
         throw new IllegalStateException("Activity must implement fragment's callbacks.");
      }

      mCallbacks = (Callbacks) activity;
   }

   /**
    * @see android.support.v4.app.ListFragment#onDetach()
    */
   @Override
   public void onDetach() {
      super.onDetach();

      // Reset the active callbacks interface to the dummy implementation.
      mCallbacks = sDummyCallbacks;
   }

   /**
    * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
    */
   @Override
   public void onListItemClick(ListView listView, View view, int position, long id) {
      super.onListItemClick(listView, view, position, id);

      // Notify the active callbacks interface (the activity, if the
      // fragment is attached to one) that an item has been selected.
      mCallbacks.onItemSelected(StateArrayAdapter.ITEMS.get(position).getId());
   }

   /**
    * @see android.support.v4.app.ListFragment#onSaveInstanceState(android.os.Bundle)
    */
   @Override
   public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      if (mActivatedPosition != ListView.INVALID_POSITION) {
         // Serialize and persist the activated item position.
         outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
      }
   }

   /**
    * Turns on activate-on-click mode. When this mode is on, list items will be
    * given the 'activated' state when touched.
    *
    * @param activateOnItemClick <code>true</code>, if item should be activated
    *                            by click, else <code>false</code>.
    */
   public void setActivateOnItemClick(boolean activateOnItemClick) {
      // When setting CHOICE_MODE_SINGLE, ListView will automatically
      // give items the 'activated' state when touched.
      getListView().setChoiceMode(activateOnItemClick
            ? ListView.CHOICE_MODE_SINGLE
            : ListView.CHOICE_MODE_NONE);
   }

   /**
    * Sets the activated position.
    *
    * @param position activated position.
    */
   private void setActivatedPosition(int position) {
      if (position == ListView.INVALID_POSITION) {
         getListView().setItemChecked(mActivatedPosition, false);
      } else {
         getListView().setItemChecked(position, true);
      }

      mActivatedPosition = position;
   }
}
