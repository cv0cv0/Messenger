package com.gr.messenger

import android.Manifest
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.gr.messenger.adapter.SimpleAdapter
import com.gr.messenger.databinding.ItemContentBinding
import com.gr.messenger.entity.MessageField
import com.gr.messenger.util.MessageCallback
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.util.*

@RuntimePermissions
class MainActivity : AppCompatActivity() {
    lateinit var adapter: SimpleAdapter<SimpleAdapter.CommonViewHolder, MessageField>
    val datas = ArrayList<MessageField>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainActivityPermissionsDispatcher.getMessageWithCheck(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    fun getMessage() {
        doAsync {
            val cursor = contentResolver.query(Uri.parse("content://sms/"), arrayOf("address", "body", "date"), null, null, "date desc")
            while (cursor.moveToNext()) {
                val field = MessageField()
                val date = cursor.getLong(cursor.getColumnIndex("date"))
                val format = if (DateUtils.isToday(date)) "HH:mm" else "MM月dd日"
                field.name.set(cursor.getString(cursor.getColumnIndex("address")))
                field.content.set(cursor.getString(cursor.getColumnIndex("body")))
                field.time.set(DateFormat.format(format, date).toString())
                datas.add(field)
            }
            cursor.close()

            uiThread {
                adapter = object : SimpleAdapter<SimpleAdapter.CommonViewHolder, MessageField>(this@MainActivity, datas, R.layout.item_content) {
                    override fun bindTo(holder: CommonViewHolder, position: Int) {
                        val binding = holder.binding as ItemContentBinding
                        binding.field = datas[position]
                    }
                }
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = adapter

                val callback = MessageCallback()
                callback.setOnItemMoveListener { fromPosition, toPosition ->
                    Collections.swap(datas, fromPosition, toPosition)
                    adapter.notifyItemMoved(fromPosition, toPosition)
                    true
                }
                callback.setOnItemMovedListener { view, position ->
                    val removedField = datas.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Snackbar.make(view, "已删除1条短信", Snackbar.LENGTH_LONG)
                            .setAction("撤消", {
                                datas.add(position, removedField)
                                adapter.notifyItemInserted(position)
                            })
                            .setActionTextColor(Color.parseColor("#eeff41"))
                            .show()
                }
                ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
            }
        }
    }
}
