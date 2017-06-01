package kr.ac.kaist.launduler

import android.support.annotation.IntDef
import android.support.annotation.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by kyukyukyu on 02/06/2017.
 */
object RxBus {
    private val subjects = HashMap<Long, PublishSubject<Any>>()
    private val disposables = HashMap<Any, CompositeDisposable>()

    const val SUBJECT_EXPLORE_SELECTED_DAY = 0L

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SUBJECT_EXPLORE_SELECTED_DAY)
    annotation class Subject

    @NonNull
    fun getSubject(@Subject subjectCode: Long) : PublishSubject<Any> {
        val subject = subjects[subjectCode] ?: run {
            subjects.put(subjectCode, PublishSubject.create())
            return subjects[subjectCode]!!
        }
        return subject
    }

    @NonNull
    fun getCompositeDisposable(@NonNull obj: Any) : CompositeDisposable {
        val compositeDisposable = disposables[obj] ?: run {
            disposables.put(obj, CompositeDisposable())
            return disposables[obj]!!
        }
        return compositeDisposable
    }

}