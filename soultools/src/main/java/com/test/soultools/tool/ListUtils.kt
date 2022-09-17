package com.test.soultools.tool

class ListUtils {
    companion object {
        /**
         * get size of list
         *
         *
         * <pre>
         * getSize(null)   =   0;
         * getSize({})     =   0;
         * getSize({1})    =   1;
        </pre> *
         *
         * @param <V>
         * @param sourceList
         * @return if list is null or empty, return 0, else return [List.size].
        </V> */
        @JvmStatic
        fun <V> getSize(sourceList: List<V>?): Int {
            return sourceList?.size ?: 0
        }

        /**
         * is null or its size is 0
         *
         *
         * <pre>
         * isEmpty(null)   =   true;
         * isEmpty({})     =   true;
         * isEmpty({1})    =   false;
        </pre> *
         *
         * @param <V>
         * @param sourceList
         * @return if list is null or its size is 0, return true, else return false.
        </V> */
        @JvmStatic
        fun <V> isEmpty(sourceList: List<V>?): Boolean {
            return sourceList == null || sourceList.isEmpty()
        }

        @JvmStatic
        fun <V> isNotEmpty(sourceList: List<V>?): Boolean {
            return sourceList != null && sourceList.isNotEmpty()
        }
    }
}