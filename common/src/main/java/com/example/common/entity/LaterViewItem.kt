package com.example.common.entity

import java.util.*

enum class ItemType {
    IMAGE,
    VIDEO,
    MUSIC,
    WEB_PAGE,
    OTHER
}

data class LaterViewItem @JvmOverloads constructor(
    var key: String = "",
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val contentUrl: String = "",
    var thumbnailUrl: String = "",
    var description: String = "",
    val tag: List<String> = emptyList(),
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val lastReadTime: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isDelete: Boolean = false,
    val isTop: Boolean = false,
    var isStar: Boolean = false,
    val folder: String = "",
    val itemType: ItemType = ItemType.OTHER
){
    fun toImageItem(): LaterViewImageItem {
        return LaterViewImageItem(
            key = key,
            id = id,
            title = title,
            content = content,
            contentUrl = contentUrl,
            thumbnailUrl = thumbnailUrl,
            description = description,
            tag = tag,
            createTime = createTime,
            updateTime = updateTime,
            lastReadTime = lastReadTime,
            isRead = isRead,
            isDelete = isDelete,
            isTop = isTop,
            isStar = isStar,
            folder = folder,
            itemType = ItemType.IMAGE
        )
    }

    fun toVideoItem(): LaterViewVideoItem {
        return LaterViewVideoItem(
            key = key,
            id = id,
            title = title,
            content = content,
            contentUrl = contentUrl,
            thumbnailUrl = thumbnailUrl,
            description = description,
            tag = tag,
            createTime = createTime,
            updateTime = updateTime,
            lastReadTime = lastReadTime,
            isRead = isRead,
            isDelete = isDelete,
            isTop = isTop,
            isStar = isStar,
            folder = folder,
            itemType = ItemType.VIDEO
        )
    }

    fun toMusicItem(): LaterViewMusicItem {
        return LaterViewMusicItem(
            key = key,
            id = id,
            title = title,
            content = content,
            contentUrl = contentUrl,
            thumbnailUrl = thumbnailUrl,
            description = description,
            tag = tag,
            createTime = createTime,
            updateTime = updateTime,
            lastReadTime = lastReadTime,
            isRead = isRead,
            isDelete = isDelete,
            isTop = isTop,
            isStar = isStar,
            folder = folder,
            itemType = ItemType.MUSIC
        )
    }

    fun toWebPageItem(): LaterViewWebPageItem {
        return LaterViewWebPageItem(
            key = key,
            id = id,
            title = title,
            content = content,
            contentUrl = contentUrl,
            thumbnailUrl = thumbnailUrl,
            description = description,
            tag = tag,
            createTime = createTime,
            updateTime = updateTime,
            lastReadTime = lastReadTime,
            isRead = isRead,
            isDelete = isDelete,
            isTop = isTop,
            isStar = isStar,
            folder = folder,
            itemType = ItemType.WEB_PAGE
        )
    }

    fun toOtherItem(): LaterViewOtherItem {
        return LaterViewOtherItem(
            key = key,
            id = id,
            title = title,
            content = content,
            contentUrl = contentUrl,
            thumbnailUrl = thumbnailUrl,
            description = description,
            tag = tag,
            createTime = createTime,
            updateTime = updateTime,
            lastReadTime = lastReadTime,
            isRead = isRead,
            isDelete = isDelete,
            isTop = isTop,
            isStar = isStar,
            folder = folder,
            itemType = ItemType.OTHER
        )
    }

    companion object{
        fun fromExitItem(item: Any): LaterViewItem {
            return when (item) {
                is LaterViewImageItem -> {
                    LaterViewItem(
                        key = item.key,
                        id = item.id,
                        title = item.title,
                        content = item.content,
                        contentUrl = item.contentUrl,
                        thumbnailUrl = item.thumbnailUrl,
                        description = item.description,
                        tag = item.tag,
                        createTime = item.createTime,
                        updateTime = item.updateTime,
                        lastReadTime = item.lastReadTime,
                        isRead = item.isRead,
                        isDelete = item.isDelete,
                        isTop = item.isTop,
                        isStar = item.isStar,
                        folder = item.folder,
                        itemType = item.itemType
                    )
                }

                is LaterViewVideoItem -> {
                    LaterViewItem(
                        key = item.key,
                        id = item.id,
                        title = item.title,
                        content = item.content,
                        contentUrl = item.contentUrl,
                        thumbnailUrl = item.thumbnailUrl,
                        description = item.description,
                        tag = item.tag,
                        createTime = item.createTime,
                        updateTime = item.updateTime,
                        lastReadTime = item.lastReadTime,
                        isRead = item.isRead,
                        isDelete = item.isDelete,
                        isTop = item.isTop,
                        isStar = item.isStar,
                        folder = item.folder,
                        itemType = item.itemType
                    )
                }

                is LaterViewMusicItem -> {
                    LaterViewItem(
                        key = item.key,
                        id = item.id,
                        title = item.title,
                        content = item.content,
                        contentUrl = item.contentUrl,
                        thumbnailUrl = item.thumbnailUrl,
                        description = item.description,
                        tag = item.tag,
                        createTime = item.createTime,
                        updateTime = item.updateTime,
                        lastReadTime = item.lastReadTime,
                        isRead = item.isRead,
                        isDelete = item.isDelete,
                        isTop = item.isTop,
                        isStar = item.isStar,
                        folder = item.folder,
                        itemType = item.itemType
                    )
                }

                is LaterViewWebPageItem -> {
                    LaterViewItem(
                        key = item.key,
                        id = item.id,
                        title = item.title,
                        content = item.content,
                        contentUrl = item.contentUrl,
                        thumbnailUrl = item.thumbnailUrl,
                        description = item.description,
                        tag = item.tag,
                        createTime = item.createTime,
                        updateTime = item.updateTime,
                        lastReadTime = item.lastReadTime,
                        isRead = item.isRead,
                        isDelete = item.isDelete,
                        isTop = item.isTop,
                        isStar = item.isStar,
                        folder = item.folder,
                        itemType = item.itemType
                    )
                }

                is LaterViewOtherItem -> {
                    LaterViewItem(
                        key = item.key,
                        id = item.id,
                        title = item.title,
                        content = item.content,
                        contentUrl = item.contentUrl,
                        thumbnailUrl = item.thumbnailUrl,
                        description = item.description,
                        tag = item.tag,
                        createTime = item.createTime,
                        updateTime = item.updateTime,
                        lastReadTime = item.lastReadTime,
                        isRead = item.isRead,
                        isDelete = item.isDelete,
                        isTop = item.isTop,
                        isStar = item.isStar,
                        folder = item.folder,
                        itemType = item.itemType
                    )
                }

                else -> {
                    LaterViewItem()
                }
            }
        }
    }
}

data class LaterViewImageItem @JvmOverloads constructor(
    var key: String = "",
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val contentUrl: String = "",
    var thumbnailUrl: String = "",
    var description: String = "",
    val tag: List<String> = emptyList(),
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val lastReadTime: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isDelete: Boolean = false,
    val isTop: Boolean = false,
    val isStar: Boolean = false,
    val folder: String = "",
    val itemType: ItemType = ItemType.IMAGE
)

data class LaterViewVideoItem @JvmOverloads constructor(
    var key: String = "",
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val contentUrl: String = "",
    var thumbnailUrl: String = "",
    var description: String = "",
    val tag: List<String> = emptyList(),
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val lastReadTime: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isDelete: Boolean = false,
    val isTop: Boolean = false,
    val isStar: Boolean = false,
    val folder: String = "",
    val itemType: ItemType = ItemType.VIDEO
)

data class LaterViewMusicItem @JvmOverloads constructor(
    var key: String = "",
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val contentUrl: String = "",
    var thumbnailUrl: String = "",
    var description: String = "",
    val tag: List<String> = emptyList(),
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val lastReadTime: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isDelete: Boolean = false,
    val isTop: Boolean = false,
    val isStar: Boolean = false,
    val folder: String = "",
    val itemType: ItemType = ItemType.MUSIC
)

data class LaterViewWebPageItem @JvmOverloads constructor(
    var key: String = "",
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val contentUrl: String = "",
    var thumbnailUrl: String = "",
    var description: String = "",
    val tag: List<String> = emptyList(),
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val lastReadTime: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isDelete: Boolean = false,
    val isTop: Boolean = false,
    val isStar: Boolean = false,
    val folder: String = "",
    val itemType: ItemType = ItemType.WEB_PAGE
)

data class LaterViewOtherItem @JvmOverloads constructor(
    var key: String = "",
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val contentUrl: String = "",
    var thumbnailUrl: String = "",
    var description: String = "",
    val tag: List<String> = emptyList(),
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val lastReadTime: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isDelete: Boolean = false,
    val isTop: Boolean = false,
    val isStar: Boolean = false,
    val folder: String = "",
    val itemType: ItemType = ItemType.OTHER
)