package com.td.framework.mvp.contract

import com.td.framework.R
import com.td.framework.model.bean.BaseCodeMsgInfo
import com.td.framework.mvp.model.BaseParamsInfo
import com.td.framework.mvp.presenter.BasePresenter
import com.td.framework.mvp.view.BaseView
import io.reactivex.Flowable

/**
 * Created by 江俊超 on 7/26/2017.
 *
 * 版本:1.0.0
 * **说明**<br></br>
 *  * 提交数据数据相关的合同
 **** */
interface CommitDataContract {
    /**
     * @param T 返回值数据类型
     */
    interface View : BaseView {

        /**
         * 提交数据成功，可以选择重写这个方法
         */
        fun commitSuccess()

        /**
         * 提交数据失败
         */
        fun commitDataFail(msg: String?)
    }


    /**
     *@param RP 请求参数类型 实现[BaseParamsInfo]接口
     */
    abstract class Presenter<V : View, in RP : BaseParamsInfo>(v: V) : BasePresenter<V>(v) {

        /**
         * 已经数据
         */
        fun commitData(commitParam: RP) {
            getCommitDataObservable(commitParam)?.apply {
                v.showLoading(R.string.commit_data_loding)
                //取消前一次请求
                unSubscribe()
                //开始请求
                request(this) {
                    if (it != null && it.code == 200) {
                        v.commitSuccess()
                        //v.complete("提交数据成功")
                    } else {
                        v.complete("")
                        v.commitDataFail(it?.msg)
                    }
                }
            }
        }


        /**
         * 提交数据
         */
        protected abstract fun getCommitDataObservable(params: RP): Flowable<BaseCodeMsgInfo>?

    }
}
