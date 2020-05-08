package com.tigerxdaphne.tappertracker.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.ListItemTappedTagBinding
import com.tigerxdaphne.tappertracker.db.TappedTag
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class TappedTagAdapter(
    private val today: LocalDate
) : ListAdapter<TappedTag, TappedTagViewHolder>(TappedTagDiffer) {

    private val lastUpdatedFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TappedTagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTappedTagBinding.inflate(inflater, parent, false)
        return TappedTagViewHolder(binding, today, lastUpdatedFormatter)
    }

    override fun onBindViewHolder(holder: TappedTagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TappedTagViewHolder(
    private val binding: ListItemTappedTagBinding,
    private val today: LocalDate,
    private val lastUpdatedFormatter: DateTimeFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tag: TappedTag) {
        val resources = binding.root.resources
        val lastTapped = tag.lastSet.format(lastUpdatedFormatter)
        val remainingTime = Period.between(tag.reminder, today).normalized()

        binding.name.text = tag.name
        binding.lastTapped.text = resources.getString(R.string.last_tapped_on, lastTapped)
        binding.duration.text = when {
            remainingTime.years > 0 -> resources.getString(R.string.remaining_years, remainingTime.years)
            remainingTime.months > 0 -> resources.getString(R.string.remaining_months, remainingTime.months)
            else -> resources.getString(R.string.remaining_days, remainingTime.days)
        }
    }
}

private object TappedTagDiffer : DiffUtil.ItemCallback<TappedTag>() {
    override fun areItemsTheSame(oldItem: TappedTag, newItem: TappedTag) =
        oldItem.id.contentEquals(newItem.id)

    override fun areContentsTheSame(oldItem: TappedTag, newItem: TappedTag) =
        oldItem == newItem
}
