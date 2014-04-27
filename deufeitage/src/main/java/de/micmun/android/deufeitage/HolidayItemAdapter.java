/*
 * *
 *  * Copyright 2014 MicMun
 *  *
 *  * This program is free software: you can redistribute it and/or modify it under
 *  * the terms of the GNU >General Public License as published by the
 *  * Free Software Foundation, either version 3 of the License, or >
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful, but
 *  * WITHOUT ANY WARRANTY; >without even the implied warranty of MERCHANTABILITY
 *  * or FITNESS FOR A PARTICULAR PURPOSE. >See the GNU General Public License
 *  * for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License along with
 *  * this program. If not, see >http://www.gnu.org/licenses/.
 *
 */

package de.micmun.android.deufeitage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.micmun.android.deufeitage.util.HolidayItem;

/**
 * Adapter for a list of holidays.
 *
 * @author: Michael Munzert
 * @version: 1.0, 06.04.14
 */
public class HolidayItemAdapter extends ArrayAdapter<HolidayItem> {
   /**
    * Creates a new HolidayItemAdapter with context and a list of holiday.
    *
    * @param context  context of the app.
    * @param holidays list of holidays.
    */
   public HolidayItemAdapter(Context context, ArrayList<HolidayItem> holidays) {
      super(context, R.layout.row_holiday_item, holidays);
   }

   /**
    * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
    */
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      // Get the data item for this position
      HolidayItem hitem = getItem(position);
      // Check if an existing view is being reused, otherwise inflate the view
      if (convertView == null) {
         convertView = LayoutInflater.from(getContext()).inflate(
               R.layout.row_holiday_item, null);
      }
      // Lookup view for data population
      TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
      TextView tvHome = (TextView) convertView.findViewById(R.id.tvDate);
      // Populate the data into the template view using the data object
      tvName.setText(hitem.getName());
      tvHome.setText(hitem.getDate());
      // Return the completed view to render on screen
      return convertView;
   }
}