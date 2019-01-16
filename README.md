# Survey App - ConstraintLayout - Dagger2 - ViewModel - Callback (Interface) - Firebase

- Constraint Layout example at res/activity_login.xml and res/activity_login_start_scene.xml
- Constraint Layout animation at src/login/LoginActivity.kt (updateConstraint method)
- ViewModel in src/login, src/organizationscreen/OrgViewModel.kt, src/surveychooser/SCViewModel.kt
	>All ViewModel dependences were injected by Dagger in src/viewmodel/ViewModelModule.kt throught ViewModelFactory (SurveyViewModelFactory)

- Firebase example at src/SurveyRepository.kt
- Communication of states between ViewModel and View throught interface src/login/AttempToLoginCallback at src/login/LoginActivity.kt
