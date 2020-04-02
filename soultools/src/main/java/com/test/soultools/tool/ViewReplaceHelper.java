package com.test.soultools.tool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.LayoutInflaterCompat;

/**
 * 高性能view替换器
 *
 * @author cd5160866
 * @date 2018/7/7
 */
public class ViewReplaceHelper {

    private int[] mBackGroundRes;

    private int[] mStyleRes;

    private Context mContext;

    private Class<? extends View>[] mComponentClasses;

    private ViewReplaceHelper() {
    }

    /**
     * 如果是用AppComponentActivity 须在Activity的onCreate之前调用
     * 因为这个方法只能被执行一次
     */
    public void replace(final BaseCatConverter catConverter) {
        LayoutInflaterCompat.setFactory2(LayoutInflater.from(mContext), new LayoutInflater.Factory2() {
            @Nullable
            @Override
            public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                return null;
            }

            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                int n = attrs.getAttributeCount();
                //无需匹配类型
                if (isMatchWithOutClass()) {
                    return getMatchResult(parent, name, context, attrs, n, null, catConverter);
                } else {
                    Class<? extends View> viewClass = getSpecificViewClass(name);
                    //当前类名和传入类名未匹配上
                    if (null == viewClass) {
                        return noMatchedResult(parent, name, context, attrs);
                    }
                    return getMatchResult(parent, name, context, attrs, n, viewClass, catConverter);
                }
            }
        });
    }

    /**
     * 当没匹配上时候，判断是否是AppComponentActivity 如果是，执行它内部执行的过的替换逻辑
     */
    private View noMatchedResult(View parent, String name, Context context, AttributeSet attrs) {
        if (context instanceof AppCompatActivity) {
            AppCompatDelegate delegate = ((AppCompatActivity) context).getDelegate();
            return delegate.createView(parent, name, context, attrs);
        }
        return null;
    }

    private boolean isMatchWithOutClass() {
        return mComponentClasses == null || mComponentClasses.length == 0;
    }

    private Class<? extends View> getSpecificViewClass(String name) {
        for (Class<? extends View> c : mComponentClasses) {
            if (c.getSimpleName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    private View getMatchResult(View parent, String name, Context context, AttributeSet attrs, int count, Class<? extends View> cls, BaseCatConverter catConverter) {
        for (int i = 0; i < count; i++) {
            //以后如果要拓展更多属性的判断，可以在这里添加条件
            int styleRes = getSpecificStyleRes(i, attrs);
            int backGroundRes = getSpecificBackGroundRes(i, attrs);
            if (styleRes > 0 || backGroundRes > 0) {
                return catConverter.onChangePrince(context, name, attrs, cls, styleRes, backGroundRes);
            }
        }
        return noMatchedResult(parent, name, context, attrs);
    }

    private int getSpecificStyleRes(int index, AttributeSet attrs) {
        if (null == mStyleRes) {
            return 0;
        }
        int res;
        for (int style : mStyleRes) {
            res = getSpecificRes(index, attrs, "style", style);
            if (res > 0) {
                return res;
            }
        }
        return 0;
    }

    private int getSpecificBackGroundRes(int index, AttributeSet attrs) {
        if (null == mBackGroundRes) {
            return 0;
        }
        int res;
        for (int backGround : mBackGroundRes) {
            res = getSpecificRes(index, attrs, "background", backGround);
            if (res > 0) {
                return res;
            }
        }
        return 0;
    }

    private int getSpecificRes(int index, AttributeSet attrs, String attributeName, int attributeRes) {
        int currentRes = attrs.getAttributeResourceValue(index, 0);
        if (attributeName.equalsIgnoreCase(attrs.getAttributeName(index)) && currentRes == attributeRes) {
            return currentRes;
        } else {
            return 0;
        }
    }

    public interface BaseCatConverter {

        /**
         * 狸猫换太子
         *
         * @param context             activity context
         * @param princeAttrs         太子view的属性
         * @param princeName          太子viewName
         * @param princeClass         太子的类名(如果传入了的话)
         * @param princeStyleRes      太子的styleRes
         * @param princeBackgroundRes 太子的BackGroundRes
         * @return 你要替换的狸猫View
         */
        View onChangePrince(Context context, String princeName, AttributeSet princeAttrs, @Nullable Class<? extends View> princeClass, @StyleRes int princeStyleRes, @DrawableRes int princeBackgroundRes);

    }

    public static class Builder {
        private int[] mBackGroundRes;
        private int[] mStyleRes;
        private Context mContext;
        private Class<? extends View>[] mComponentClasses;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder orBackGround(@DrawableRes int... mBackGroundRes) {
            this.mBackGroundRes = mBackGroundRes;
            return this;
        }

        public Builder orStyle(@StyleRes int... styleRes) {
            this.mStyleRes = styleRes;
            return this;
        }

        @SafeVarargs
        public final Builder withTargetViewClass(Class<? extends View>... componentClass) {
            this.mComponentClasses = componentClass;
            return this;
        }

        public ViewReplaceHelper build() {
            ViewReplaceHelper helper = new ViewReplaceHelper();
            helper.mBackGroundRes = mBackGroundRes;
            helper.mStyleRes = mStyleRes;
            helper.mContext = mContext;
            helper.mComponentClasses = mComponentClasses;
            return helper;
        }
    }

}
