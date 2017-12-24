package tech.asfaw.ethiochallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.asfaw.ethiochallenge.models.Challenge;
import tech.asfaw.ethiochallenge.models.ChallengeLab;
import tech.asfaw.ethiochallenge.models.Person;
import tech.asfaw.ethiochallenge.models.UserLab;


public class ChallengeFragment extends Fragment implements Button.OnClickListener{
    private static final String TAG = "ChallengeFragment";
    public static final String DEFAULT_PLAYER_NAME = "Player 1";
    public static final String QUESTION_STRING = "ትያቄ :";

    @SuppressWarnings("FieldCanBeLocal")
    private ChallengeLab mChallengeLab;
    @SuppressWarnings("FieldCanBeLocal")
    private List<Challenge> mChallenges;
    @SuppressWarnings("FieldCanBeLocal")
    private Person mPerson;
    private int mCurrentQuestion;
    private HashMap<Challenge, String> mChallengeResult;
    private Challenge mCurrentChallenge;


    @BindView(R.id.text_view_fragment_main)
    TextView mFirstText;
    @BindView(R.id.question_text)
    TextView mQuestionText;
    @BindView(R.id.first_choice)
    Button mFirstChoiceButton;
    @BindView(R.id.second_choice)
    Button mSecondChoiceButton;
    @BindView(R.id.third_choice)
    Button mThirdChoiceButton;
    @BindView(R.id.fourth_choice)
    Button mFourthChoiceButton;

    public ChallengeFragment(){
        // Required public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPerson = new Person(DEFAULT_PLAYER_NAME);
        mCurrentQuestion = 0;
        mChallengeResult = new HashMap<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);
        ButterKnife.bind(this, view);
        mFirstText.setTextSize(20);

        mChallengeLab = new ChallengeLab(getActivity());
        mChallenges = mChallengeLab.getChallenges();

        mFirstChoiceButton.setOnClickListener(this);
        mSecondChoiceButton.setOnClickListener(this);
        mThirdChoiceButton.setOnClickListener(this);

        nextQuestion(mCurrentQuestion);

//        Toast.makeText(getActivity(), tchallenge.getQuestion(), Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.first_choice:
                mChallengeResult.put(mCurrentChallenge, mFirstChoiceButton.getText().toString());
                break;
            case R.id.second_choice:
                mChallengeResult.put(mCurrentChallenge, mSecondChoiceButton.getText().toString());
                break;
            case R.id.third_choice:
                mChallengeResult.put(mCurrentChallenge, mThirdChoiceButton.getText().toString());
                break;
            case R.id.fourth_choice:
                mChallengeResult.put(mCurrentChallenge, mFourthChoiceButton.getText().toString());
                break;
            default:
                Log.i(TAG, "I don't even know how there is no correct match for the selected button");

        }

        if (mCurrentQuestion < mChallenges.size()-1){
            mCurrentQuestion++;
            nextQuestion(mCurrentQuestion);
        } else{
            //TODO: Implement results page.

            calculateScore();
            UserLab.getLab(getActivity()).saveUser(mPerson);
            Toast.makeText(getActivity(), "You're done! \\o/", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            getActivity().startActivity(intent);
        }
    }
    private void calculateScore(){
        //TODO move somwhere else later
        int correct = 0;
        int wrong = 0;
        for(Map.Entry<Challenge, String> entry : mChallengeResult.entrySet()){

            if (entry.getKey().getAnswer().equals(entry.getValue())){
                correct ++;
            }else {
                wrong ++;
            }
        }
        mPerson.setCorrect(correct);
        mPerson.setWrong(wrong);
    }
    private void nextQuestion(int num){
        mFirstText.setText(QUESTION_STRING + (num+1));
        mCurrentChallenge = mChallenges.get(num);
        mQuestionText.setText(mCurrentChallenge.getQuestion());
        mFirstChoiceButton.setText(mCurrentChallenge.getOptions().get(0));
        mSecondChoiceButton.setText(mCurrentChallenge.getOptions().get(1));
        mThirdChoiceButton.setText(mCurrentChallenge.getOptions().get(2));
        mFourthChoiceButton.setText(mCurrentChallenge.getOptions().get(3));


    }

    /**
     * Idea! Put in a link within this class to randomly pick, either the buttons or the options
     * and place them randomly with the buttons. maybe take some of the code from CSVParser.java
     * the new RandomGenerator can help with what we wanna do...
     */

}