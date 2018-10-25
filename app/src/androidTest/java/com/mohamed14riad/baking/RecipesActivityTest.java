package com.mohamed14riad.baking;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mohamed14riad.baking.activities.RecipesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    @Rule
    public ActivityTestRule<RecipesActivity> activityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void firstTest() {
        Espresso.registerIdlingResources(activityTestRule.getActivity().getIdlingResource());

        onView(withId(R.id.recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredient_fab)).perform(click());
        onView(allOf(withId(R.id.ingredient_description)
                , atPosition(withId(R.id.ingredient_list), 0)
                , withText("Graham Cracker crumbs")
                , isDisplayed()));
        pressBack();

        onView(withId(R.id.steps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(allOf(withId(R.id.step_description)
                , withText("3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.")
                , isDisplayed()));
        onView(allOf(withId(R.id.skip_next), withContentDescription("Next"), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.skip_previous), withContentDescription("Previous"), isDisplayed())).perform(click());
        pressBack();
        pressBack();
    }

    @Test
    public void secondTest() {
        Espresso.registerIdlingResources(activityTestRule.getActivity().getIdlingResource());

        onView(withId(R.id.recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.ingredient_fab)).perform(click());
        onView(withId(R.id.ingredient_list)).perform(RecyclerViewActions.scrollToPosition(6));
        onView(allOf(withId(R.id.ingredient_description)
                , atPosition(withId(R.id.ingredient_list), 6)
                , withText("whole milk")
                , isDisplayed()));
        pressBack();

        onView(withId(R.id.steps_list)).perform(RecyclerViewActions.scrollToPosition(12));
        onView(withId(R.id.steps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(12, click()));
        onView(allOf(withId(R.id.step_description)
                , withText("12. With the mixer still running, pour the melted chocolate into the buttercream. Then add the remaining tablespoon of vanilla and 1/2 teaspoon of salt. Beat at high speed for 30 seconds to ensure the buttercream is well-mixed.")
                , isDisplayed()));
        onView(allOf(withId(R.id.skip_previous), withContentDescription("Previous"), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.skip_next), withContentDescription("Next"), isDisplayed())).perform(click());
        pressBack();
        pressBack();
    }

    @Test
    public void tabletTest() {
        Espresso.registerIdlingResources(activityTestRule.getActivity().getIdlingResource());

        onView(withId(R.id.recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredient_fab)).perform(click());
        onView(allOf(withId(R.id.ingredient_description)
                , atPosition(withId(R.id.ingredient_list), 0)
                , withText("Graham Cracker crumbs")
                , isDisplayed()));
        pressBack();

        onView(withId(R.id.steps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(allOf(withId(R.id.step_description)
                , withText("3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.")
                , isDisplayed()));
        onView(allOf(withId(R.id.skip_next), withContentDescription("Next"), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.skip_previous), withContentDescription("Previous"), isDisplayed())).perform(click());
        pressBack();
    }

    private Matcher<View> atPosition(final Matcher<View> viewMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ViewParent viewParent = view.getParent();
                return viewParent instanceof ViewGroup
                        && viewMatcher.matches(viewParent)
                        && view.equals(((ViewGroup) viewParent).getChildAt(position));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("At position: " + position);
                viewMatcher.describeTo(description);
            }
        };
    }
}
