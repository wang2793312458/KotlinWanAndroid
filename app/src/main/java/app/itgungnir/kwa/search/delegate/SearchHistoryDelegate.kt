package app.itgungnir.kwa.search.delegate

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import app.itgungnir.kwa.R
import app.itgungnir.kwa.common.redux.AppRedux
import app.itgungnir.kwa.common.redux.ClearSearchHistory
import app.itgungnir.kwa.common.widget.easy_adapter.BaseDelegate
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter
import app.itgungnir.kwa.common.widget.flex.FlexView
import app.itgungnir.kwa.common.widget.status_view.StatusView
import app.itgungnir.kwa.search.SearchState
import kotlinx.android.synthetic.main.listitem_search_history.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

class SearchHistoryDelegate(
    private val keyClickCallback: (String) -> Unit
) : BaseDelegate<SearchState.SearchHistoryVO>() {

    override fun layoutId(): Int = R.layout.listitem_search_history

    override fun onCreateVH(container: View) {
        container.apply {
            // Clear Button
            clear.setOnClickListener {
                AppRedux.instance.dispatch(ClearSearchHistory, true)
                statusView.empty { }
                clear.apply {
                    isEnabled = false
                    textColor = Color.parseColor("#FFC2C2C2")
                }
            }
            // Status View
            statusView.addDelegate(StatusView.Status.SUCCEED, R.layout.status_view_flex) {
                it.findViewById<FlexView>(R.id.children).bind<String>(
                    layoutId = R.layout.listitem_search_text,
                    items = listOf(),
                    render = { view, data ->
                        view.findViewById<TextView>(R.id.name).apply {
                            text = data
                            backgroundColor = Color.parseColor("#FFC2C2C2")
                            textColor = Color.parseColor("#FF373737")
                            setOnClickListener {
                                keyClickCallback.invoke(data)
                            }
                        }
                    }
                )
            }.addDelegate(StatusView.Status.EMPTY, R.layout.status_view_flex_empty) {
                it.findViewById<TextView>(R.id.tip).text = "快来搜点干货吧( •̀ ω •́ )✧"
            }
        }
    }

    override fun onBindVH(
        item: SearchState.SearchHistoryVO,
        holder: EasyAdapter.VH,
        position: Int,
        payloads: MutableList<Bundle>
    ) {

        holder.render(item) {

            if (item.data.isEmpty()) {
                statusView.empty { }
                clear.apply {
                    isEnabled = false
                    textColor = Color.parseColor("#FFC2C2C2")
                }
            } else {
                statusView.succeed {
                    (it as FlexView).update(item.data)
                }
                clear.apply {
                    isEnabled = true
                    textColor = Color.parseColor("#FFFFAB00")
                }
            }
        }
    }
}