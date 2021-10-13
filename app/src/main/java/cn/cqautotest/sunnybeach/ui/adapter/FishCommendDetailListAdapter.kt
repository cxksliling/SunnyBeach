package cn.cqautotest.sunnybeach.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.cqautotest.sunnybeach.R
import cn.cqautotest.sunnybeach.databinding.FishPondDetailCommendListBinding
import cn.cqautotest.sunnybeach.model.FishPondComment
import cn.cqautotest.sunnybeach.util.DateHelper
import com.bumptech.glide.Glide

/**
 * author : A Lonely Cat
 * github : https://github.com/anjiemo/SunnyBeach
 * time   : 2021/09/18
 * desc   : 摸鱼评论列表适配器
 */
class FishCommendDetailListAdapter : RecyclerView.Adapter<FishDetailCommendListViewHolder>() {

    private lateinit var mData: FishPondComment.FishPondCommentItem

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: FishPondComment.FishPondCommentItem) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FishDetailCommendListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FishPondDetailCommendListBinding.inflate(inflater, parent, false)
        return FishDetailCommendListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FishDetailCommendListViewHolder, position: Int) {
        val item = mData.subComments[position]
        val itemView = holder.itemView
        val binding = holder.binding
        val flAvatarContainer = binding.flAvatarContainer
        val ivAvatar = binding.ivFishPondAvatar
        val tvNickname = binding.cbFishPondNickName
        val tvDesc = binding.tvFishPondDesc
        val tvReply = binding.tvReplyMsg
        val tvBuildReplyMsgContainer = binding.tvBuildReplyMsgContainer
        val context = itemView.context
        flAvatarContainer.background = if (item.vip) ContextCompat.getDrawable(
            context,
            R.drawable.avatar_circle_vip_ic
        ) else null
        Glide.with(holder.itemView)
            .load(item.avatar)
            .placeholder(R.mipmap.ic_default_avatar)
            .error(R.mipmap.ic_default_avatar)
            .circleCrop()
            .into(ivAvatar)
        tvNickname.setTextColor(
            ContextCompat.getColor(
                context, if (item.vip) {
                    R.color.pink
                } else {
                    R.color.black
                }
            )
        )
        tvNickname.text = item.getNickName()
        // 摸鱼详情列表的时间没有精确到秒
        tvDesc.text = "${item.position} · " +
                DateHelper.transform2FriendlyTimeSpanByNow("${item.createTime}:00")
        tvReply.text = getBeautifiedFormat(item, mData)
        tvBuildReplyMsgContainer.visibility = View.GONE
    }

    private fun getBeautifiedFormat(
        subComment: FishPondComment.FishPondCommentItem.SubComment,
        item: FishPondComment.FishPondCommentItem
    ): Spanned {
        val whoReplied = ""
        val wasReplied = subComment.targetUserNickname
        val content = whoReplied + "回复" + wasReplied + "：" + subComment.content
        val spannableString = SpannableString(content)
        val color = Color.parseColor("#045FB2")
        spannableString.setSpan(
            ForegroundColorSpan(color),
            content.indexOf(whoReplied),
            content.indexOf("回复"),
            SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(color),
            content.indexOf(wasReplied),
            content.indexOf(wasReplied) + wasReplied.length,
            SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        )
        return spannableString
    }

    override fun getItemCount(): Int = mData.subComments.size
}