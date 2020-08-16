package com.deals_notifier.post.model

import java.util.*

class SortedPostList : ArrayList<Post>(), Comparator<Post> {


    fun removeAllOlderThan(date: Date) {
        val index = Collections.binarySearch(this, Post(id = "", date = date), this)
        val firstIndexToRemove =
            if (index < 0) {
                //Not found
                (index + 1) * (-1)
            } else {
                //Found
                index
            }

        if (firstIndexToRemove > -1) {
            //If there are indexes to remove, get rid of them
            removeRange(
                firstIndexToRemove,
                this.size

            )
        }
    }


    /**
     * Does Binary Search Add, while detecting duplicates
     */
    override fun add(element: Post): Boolean {
        //Will return index or ((insertion point)*(-1) -1)
        val index = Collections.binarySearch(this, element, this)

        //Important to avoid duplicate
        return if (index < 0) {
            super.add((index + 1) * (-1), element)
            true
        } else {
            false
        }
    }

    /**
     * Use @see com.deals_notifier.post.model.SortedPostList.add(T) instead
     */
    override fun add(index: Int, element: Post) {
        throw java.lang.UnsupportedOperationException()
    }

    override fun addAll(elements: Collection<Post>): Boolean {
        for (element: Post in elements) {
            this.add(element)
        }
        return true
    }

    /**
     * Use @see com.deals_notifier.post.model.SortedPostList.addAll(java.util.Collection<? extends T>) instead
     */
    override fun addAll(index: Int, elements: Collection<Post>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun indexOf(element: Post): Int {
        return Collections.binarySearch(this, element, this).coerceAtLeast(-1)
    }

    /**
     * This is a sorted list where duplicate elements are forbidden
     *
     * Use @see com.deals_notifier.post.model.SortedPostList.addAll(java.util.Collection<? extends T>) instead
     */
    override fun lastIndexOf(element: Post): Int {
        throw java.lang.UnsupportedOperationException()
    }

    override fun contains(element: Post): Boolean {
        return indexOf(element) > -1
    }

    override fun compare(p0: Post?, p1: Post?): Int {
        if (p0 != null && p1 != null) {
            val result = p1.date.compareTo(p0.date)
            if (result == 0) {
                return p0.id.compareTo(p1.id)
            }
            return result
        } else if (p0 != null) {
            return 1
        } else if (p1 != null) {
            return -1
        } else {
            return 0
        }
    }


}