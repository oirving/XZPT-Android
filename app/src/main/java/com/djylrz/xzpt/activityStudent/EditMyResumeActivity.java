package com.djylrz.xzpt.activityStudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.EducationViewAdapter;
import com.djylrz.xzpt.utils.EducationViewItem;
import com.djylrz.xzpt.utils.ExperienceViewAdapter;
import com.djylrz.xzpt.utils.ExperienceViewItem;
import com.djylrz.xzpt.utils.JobIntentViewItem;
import com.djylrz.xzpt.utils.JobIntentionViewAdapter;
import com.djylrz.xzpt.utils.ProjectViewAdapter;
import com.djylrz.xzpt.utils.ProjectItem;

import java.util.ArrayList;
import java.util.List;

public class EditMyResumeActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView userName;//姓名
    private TextView basicInfo;//基本信息
    private ImageView edit;//名字旁的编辑
    private ImageView headView;//头像
    private TextView jobState;//求职状态
    private ImageView jobStateNext;//求职状态下一步图标
    private RecyclerView jobIntentionList;//求职期望
    private TextView addJobIntention;//添加求职期望
    private RecyclerView experienceList;//实习经历
    private TextView addExperience;//添加求职期望
    private RecyclerView projectList;//项目经历
    private TextView addProject;//添加项目经历
    private RecyclerView educationList;//教育经历
    private TextView addEducation;//添加教育经历
    private Button previewOrDownload;//预览/下载
    private JobIntentionViewAdapter jobIntentionViewAdapter;
    private EducationViewAdapter educationViewAdapter;
    private ExperienceViewAdapter experienceViewAdapter;
    private ProjectViewAdapter projectViewAdapter;
    private List<JobIntentViewItem> jobIntentViewItemArrayList= new ArrayList<>();
    private List<EducationViewItem> educationViewItemList = new ArrayList<>();
    private List<ExperienceViewItem> experienceViewItemList = new ArrayList<>();
    private List<ProjectItem> projectViewItemList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
    private LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
    private LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_resume);
        userName = (TextView)findViewById(R.id.user_name);
        edit = (ImageView)findViewById(R.id.edit_imageview);
        edit.setOnClickListener(this);
        basicInfo = (TextView)findViewById(R.id.basic_info);
        jobState = (TextView)findViewById(R.id.job_state);
        jobStateNext = (ImageView)findViewById(R.id.job_state_next);

        jobIntentionList = (RecyclerView)findViewById(R.id.job_intention_list);
        jobIntentionViewAdapter = new JobIntentionViewAdapter(jobIntentViewItemArrayList);
        jobIntentionList.setAdapter(jobIntentionViewAdapter);
        jobIntentionList.setLayoutManager(linearLayoutManager);

        educationList = (RecyclerView)findViewById(R.id.education_list);
        educationViewAdapter = new EducationViewAdapter(educationViewItemList);
        educationList.setAdapter(educationViewAdapter);
        educationList.setLayoutManager(linearLayoutManager2);

        experienceList = (RecyclerView)findViewById(R.id.experience_list);
        experienceViewAdapter = new ExperienceViewAdapter(experienceViewItemList);
        experienceList.setAdapter(experienceViewAdapter);
        experienceList.setLayoutManager(linearLayoutManager3);

        projectList = (RecyclerView)findViewById(R.id.project_list);
        projectViewAdapter = new ProjectViewAdapter(projectViewItemList);
        projectList.setAdapter(projectViewAdapter);
        projectList.setLayoutManager(linearLayoutManager4);

        addJobIntention = (TextView)findViewById(R.id.add_job_intention);
        addJobIntention.setOnClickListener(this);
        experienceList = (RecyclerView)findViewById(R.id.experience_list);
        addExperience = (TextView)findViewById(R.id.add_experience);
        addExperience.setOnClickListener(this);
        projectList = (RecyclerView)findViewById(R.id.project_list);
        addProject = (TextView)findViewById(R.id.add_project);
        addProject.setOnClickListener(this);
        educationList = (RecyclerView)findViewById(R.id.education_list);
        addEducation = (TextView)findViewById(R.id.add_education);
        addEducation.setOnClickListener(this);
        previewOrDownload = (Button)findViewById(R.id.preview);
        previewOrDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //预览/下载
            case R.id.preview:
//                intent = new Intent(EditMyResumeActivity.this,IntentResumeFileActivity.class);
//                startActivity(intent);
                break;
                //跳转到个人信息进行编辑
            case R.id.edit_imageview:
                intent = new Intent(EditMyResumeActivity.this,PersonalInformation.class);
                startActivity(intent);
                break;
            //添加教育经历
            case R.id.add_education:
                addEducation("","","","");
                break;
                //添加实习经历
            case R.id.add_experience:
                addExperience("","");
                break;
                //添加项目经历
            case R.id.add_project:
                addProject("");
                break;
                //添加求职期望，限定一个
            case R.id.add_job_intention:
                addJobIntention("","","","","");
                break;
                default:
                    break;
        }
    }

    //todo 初始化界面 ->小榕
    public void initPage(User user) {
        userName.setText(user.getUserName());
        basicInfo.setText("-"+String.valueOf(user.getAge())+"-"+user.getHighestEducation());

    }

    /**
     *
     * @param school 学校
     * @param degree 学位
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public void addEducation(String school,String degree,String startTime,String endTime) {
        EducationViewItem educationViewItem = new EducationViewItem(school,degree,startTime,endTime,R.drawable.right);
        educationViewItemList.add(educationViewItem);
        educationList.scrollToPosition(educationViewItemList.size()-1);
        addEducation.setVisibility(View.INVISIBLE);
    }

    /**
     *
     * @param jobName 岗位名称
     * @param basicSalary 工资底线
     * @param topSalary 工资上限
     * @param jobLocation 工作地点
     * @param jobIndustry 工作行业
     */
    public void addJobIntention(String jobName,String basicSalary,String topSalary,String jobLocation,String jobIndustry) {
        JobIntentViewItem jobIntentViewItem = new JobIntentViewItem(jobName,basicSalary,topSalary,jobLocation,jobIndustry,R.drawable.right);
        jobIntentViewItemArrayList.add(jobIntentViewItem);
        jobIntentionViewAdapter.notifyItemInserted(jobIntentViewItemArrayList.size()-1);
        jobIntentionList.scrollToPosition(jobIntentViewItemArrayList.size()-1);
        addJobIntention.setVisibility(View.INVISIBLE);
    }

    /**
     *
     * @param companyName 实习公司名
     * @param position 实习职位
     */
    public void addExperience(String companyName,String position){
        ExperienceViewItem experienceViewItem = new ExperienceViewItem(companyName,position,R.drawable.right);
        experienceViewItemList.add(experienceViewItem);
        experienceList.scrollToPosition(experienceViewItemList.size()-1);
    }

    /**
     *
     * @param projectName 项目名称
     */
    public void addProject(String projectName) {
        ProjectItem projectViewItem = new ProjectItem(projectName,R.drawable.right);
        projectViewItemList.add(projectViewItem);
        projectList.scrollToPosition(projectViewItemList.size()-1);
    }
}
