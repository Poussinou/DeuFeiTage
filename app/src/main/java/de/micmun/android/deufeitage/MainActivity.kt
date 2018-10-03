package de.micmun.android.deufeitage

import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.micmun.android.deufeitage.adapter.HolidayAdapter
import de.micmun.android.deufeitage.adapter.YearRecyclerAdapter
import de.micmun.android.deufeitage.adapter.YearViewHolder
import de.micmun.android.deufeitage.model.YearItem
import de.micmun.android.deufeitage.utils.HolidayCalculator
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.time.LocalDate
import java.util.*

/**
 * MainActivity of the app.
 *
 * @author MicMun
 * @version 1.0, 07.08.18
 */
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
        YearViewHolder.OnYearItemSelectedListener {

    private var holidayRv: RecyclerView? = null
    private var yearAdapter: YearRecyclerAdapter? = null
    private var holidayCalculator: HolidayCalculator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // state selection
        val stateAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
                R.array.states_of_germany, android.R.layout.simple_spinner_item)
        stateSpinner.adapter = stateAdapter
        stateSpinner.onItemSelectedListener = this

        // holiday calculator
        holidayCalculator = HolidayCalculator(this)

        // year selection
        initYearSelector()

        // holiday view
        holidayRv = holidayView
        holidayRv!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        Log.d("MainActivity", parent.getItemAtPosition(pos).toString())
    }


    override fun onNothingSelected(parent: AdapterView<*>) {

    }

    /**
     * Init year selector to show years
     */
    private fun initYearSelector() {
        val years = mutableListOf<YearItem>()
        val currentYear = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().year
        } else {
            Calendar.getInstance().get(Calendar.YEAR)
        }
        var current: YearItem? = null

        for (i in currentYear - 4..currentYear + 4) {
            if (i == currentYear) {
                current = YearItem(i)
                years.add(current)
            } else
                years.add(YearItem(i))
        }

        val selectedPos = years.indexOf(current)

        yearAdapter = YearRecyclerAdapter(years.toTypedArray(), this)
        yearSelector.adapter = yearAdapter
        yearSelector.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)
        yearSelector.postDelayed({
            yearSelector.findViewHolderForAdapterPosition(selectedPos)?.itemView?.performClick()
        },
                20)
    }

    override fun onYearItemSelected(item: YearItem) {
        Log.d("MainActivity", item.toString())


        val adapter = HolidayAdapter(holidayCalculator!!.getHolidays(item.year))
        holidayRv!!.adapter = adapter
    }

    override fun openOrCreateDatabase(p0: String?, p1: Int,
                                      p2: SQLiteDatabase.CursorFactory?): SQLiteDatabase {
        return SQLiteDatabase.openOrCreateDatabase(File(p0), p2)
    }

}