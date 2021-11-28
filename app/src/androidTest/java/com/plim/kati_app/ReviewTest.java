giotpackage com.plim.kati_app;

import com.plim.kati_app.kati.domain.main.MainActivity;
import com.plim.kati_app.testUtil.KatiTestClass;
import com.plim.kati_app.testUtil.TestTool;

import org.junit.Test;

public class ReviewTest extends KatiTestClass<MainActivity> {
    public static final String REVIEW_TEST_REVIEW_TEXT_1="좋아요";
    public static final String REVIEW_TEST_REVIEW_TEXT_2="생각보다는 좋은 것 같아요";

    @Override
    protected Class getTargetActivityClass() { return MainActivity.class; }

    @Override
    protected void beforeTest() {
        try{
            TestTool.doLoginIfNotLogin();
        }catch (Exception e) { }
    }

    //리뷰쓰기
    @Test
    public void createNewReview() throws InterruptedException {
        TestTool.sleep();
        this.clickView(R.id.action_search);
        TestTool.sleep();
        this.clickView(20024);
        TestTool.sleep();
        TestTool.sleep();
        TestTool.sleep();
        TestTool.sleep();
        this.clickView(R.id.newFoodItem_productNameTextView);
        this.clickView(5002);
        TestTool.sleep();
        this.clickView(R.id.reviewSummaryWidget_writeReviewButton);

        //리뷰 작성
        TestTool.sleep();
        this.replaceViewText(R.id.reviewActivity_reviewEditText,REVIEW_TEST_REVIEW_TEXT_1);
        this.clickView(R.id.reviewActivity_submitButton);

        this.checkTextExists(REVIEW_TEST_REVIEW_TEXT_1);
    }



    //리뷰 수정하기
    @Test
    public void updateNewReview() throws InterruptedException {

        //리뷰관리로 들어가기
        TestTool.sleep();
        this.clickView(R.id.myKatiFragment_reviewItem);

        //수정버튼 클릭
        TestTool.sleep();
        TestTool.sleep();
        this.clickView(R.id.review_edit);

        //에딧텍스트 편집
        this.replaceViewText(R.id.reviewActivity_reviewEditText,REVIEW_TEST_REVIEW_TEXT_2);
        //등록완료 누르기
        this.clickView(R.id.reviewActivity_submitButton);

        //텍스트 존재 확인
        TestTool.sleep();
        this.checkTextExists(REVIEW_TEST_REVIEW_TEXT_2);

    }


    //리뷰 삭제하기
    @Test
    public void deleteNewReview() throws InterruptedException {
        //리뷰관리로 들어가기
        TestTool.sleep();
        this.clickView(R.id.myKatiFragment_reviewItem);

        //삭제버튼 클릭
        TestTool.sleep();
        this.clickView(R.id.review_delete);

        //텍스트 존재 확인
        TestTool.sleep();
        this.checkTextNotExists(REVIEW_TEST_REVIEW_TEXT_2);
    }



}
