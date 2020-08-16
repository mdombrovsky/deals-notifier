package com.deals_notifier

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class SortedPostListTest {
    private val date1 = Date(1597435540000)
    private val date2 = Date(1597521940000)
    private val date3 = Date(1597608340000)
    private val date4 = Date(1597651540000)
    private val date5 = Date(1597694740000)
    private val date6 = Date(1597696168000)
    private val post02 = Post(title = "", id = "", date = date2)
    private val post03 = Post(title = "", id = "", date = date3)
    private val post1 = Post(title = "1", id = "1", date = date2)
    private val post2 = Post(title = "2", id = "2", date = date2)
    private val post3 = Post(title = "3", id = "3", date = date2)
    private val post4 = Post(title = "4", id = "4", date = date2)
    private val post5 = Post(title = "5", id = "5", date = date1)
    private val post6 = Post(title = "6", id = "6", date = date3)
    private val post7 = Post(title = "7", id = "7", date = date3)
    private val post8 = Post(title = "8", id = "8", date = date5)
    private val post9 = Post(title = "8", id = "8", date = date6)


    @Test
    fun testInsertion() {

        val sortedPostList = SortedPostList()
        sortedPostList.add(post7)

        try {
            sortedPostList.add(1, post2)
        } catch (e: UnsupportedOperationException) {

        }
        sortedPostList.add(post4)

        sortedPostList.addAll(arrayListOf(post3, post1, post4, post5, post6, post02))

        assertArrayEquals(
            sortedPostList.toArray(),
            arrayOf(post6, post7, post02, post1, post3, post4, post5)
        )
    }

    @Test
    fun testRemoval1() {
        val sortedPostList = SortedPostList()


        sortedPostList.addAll(arrayListOf(post1, post2, post3, post4, post5, post6, post7, post8))
        sortedPostList.removeAllOlderThan(date4)
        assertArrayEquals(sortedPostList.toArray(), arrayOf(post8))


    }

    @Test
    fun testRemoval2() {
        val sortedPostList = SortedPostList()

        sortedPostList.addAll(arrayListOf(post1, post2, post3, post4, post5, post6, post7, post8))
        sortedPostList.removeAllOlderThan(date3)

        assertArrayEquals(sortedPostList.toArray(), arrayOf(post8))
    }

    @Test
    fun testRemoval3() {
        val sortedPostList = SortedPostList()

        sortedPostList.addAll(
            arrayListOf(
                post1,
                post2,
                post3,
                post4,
                post5,
                post6,
                post7,
                post8,
                post03
            )
        )
        sortedPostList.removeAllOlderThan(date3)

        assertArrayEquals(sortedPostList.toArray(), arrayOf(post8))
    }

    @Test
    fun testRemoval4() {
        val sortedPostList = SortedPostList()

        sortedPostList.addAll(arrayListOf(post1, post2, post3, post4, post5, post6, post7, post03))
        sortedPostList.removeAllOlderThan(date3)

        assertArrayEquals(sortedPostList.toArray(), arrayOf<Post>())
    }

    @Test
    fun testRemoval5() {
        val sortedPostList = SortedPostList()


        sortedPostList.addAll(
            arrayListOf(
                post1,
                post2,
                post3,
                post9,
                post9,
                post4,
                post5,
                post6,
                post7,
                post8
            )
        )
        sortedPostList.removeAllOlderThan(date4)
        assertArrayEquals(sortedPostList.toArray(), arrayOf(post9, post8))
    }

    @Test
    fun testRemoval6() {
        val sortedPostList = SortedPostList()

        sortedPostList.addAll(
            arrayListOf(
                post1,
                post2,
                post9,
                post3,
                post4,
                post5,
                post6,
                post7,
                post8
            )
        )
        sortedPostList.removeAllOlderThan(date3)

        assertArrayEquals(sortedPostList.toArray(), arrayOf(post9, post8))
    }

    @Test
    fun testIndexOf() {
        val sortedPostList = SortedPostList()

        sortedPostList.addAll(arrayListOf(post1, post2, post3, post4, post5, post6, post7, post8))

        assertEquals(sortedPostList.indexOf(post3), 5)
    }

}