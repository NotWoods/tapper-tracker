package com.tigerxdaphne.tappertracker.pages.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.ListItemTappedTagBinding
import com.tigerxdaphne.tappertracker.db.TappedTag
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import com.tigerxdaphne.tappertracker.db.equalTags
import java.time.Clock
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TappedTagAdapter(
    private val clock: Clock
) : ListAdapter<TappedTag, TappedTagViewHolder>(TappedTagDiffer) {

    private val lastUpdatedFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TappedTagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTappedTagBinding.inflate(inflater, parent, false)
        return TappedTagViewHolder(binding, lastUpdatedFormatter)
    }

    override fun onBindViewHolder(holder: TappedTagViewHolder, position: Int) {
        holder.bind(getItem(position), today = LocalDate.now(clock))
    }
}

class TappedTagViewHolder(
    private val binding: ListItemTappedTagBinding,
    private val lastUpdatedFormatter: DateTimeFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tag: TappedTag, today: LocalDate) {
        val resources = binding.root.resources
        val lastTapped = tag.lastSet.format(lastUpdatedFormatter)
        val remainingTime = Period.between(today, tag.reminder).normalized()

        binding.name.text = tag.name
        binding.lastTapped.text = resources.getString(R.string.last_tapped_on, lastTapped)
        binding.duration.text = when {
            tag.isStopped ->
                resources.getString(R.string.stopped)
            remainingTime.years > 0 ->
                resources.getString(R.string.remaining_years, remainingTime.years)
            remainingTime.months > 0 ->
                resources.getString(R.string.remaining_months, remainingTime.months)
            remainingTime.days > 0 ->
                resources.getQuantityString(R.plurals.remaining_days, remainingTime.days, remainingTime.days)
            remainingTime.days == 0 ->
                resources.getString(R.string.remaining_none)
            else ->
                resources.getString(R.string.remaining_negative)
        }
    }
}

private object TappedTagDiffer : DiffUtil.ItemCallback<TappedTag>() {
    override fun areItemsTheSame(oldItem: TappedTag, newItem: TappedTag) =
        oldItem.id.contentEquals(newItem.id)

    override fun areContentsTheSame(oldItem: TappedTag, newItem: TappedTag) =
        equalTags(oldItem, newItem)
}
