import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CategorySpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        // Apply space to all sides of each item
        outRect.right = space


    }
}
