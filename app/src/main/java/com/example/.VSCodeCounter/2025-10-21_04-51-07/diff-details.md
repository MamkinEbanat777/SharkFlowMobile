# Diff Details

Date : 2025-10-21 04:51:07

Directory c:
\\Users\\karen\\AndroidStudioProjects\\SharkFlow\\app\\src\\main\\java\\com\\example\\sharkflow

Total : 78 files, 81 codes, 0 comments, 34 blanks, all 115 lines

[Summary](results.md) / [Details](details.md) / [Diff Summary](diff.md) / Diff Details

## Files

| filename                                                                                                                                                  | language | code | comment | blank | total |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------|:---------|-----:|--------:|------:|------:|
| [sharkflow/MainActivity.kt](/sharkflow/MainActivity.kt)                                                                                                   | Kotlin   |   -4 |       0 |     0 |    -4 |
| [sharkflow/data/api/dto/user/DeleteRequestDto.kt](/sharkflow/data/api/dto/user/DeleteRequestDto.kt)                                                       | Kotlin   |   -6 |       0 |    -3 |    -9 |
| [sharkflow/data/api/dto/user/DeleteUserDto.kt](/sharkflow/data/api/dto/user/DeleteUserDto.kt)                                                             | Kotlin   |    6 |       0 |     3 |     9 |
| [sharkflow/data/api/dto/user/UpdateAvatarRequestDto.kt](/sharkflow/data/api/dto/user/UpdateAvatarRequestDto.kt)                                           | Kotlin   |  -12 |       0 |    -4 |   -16 |
| [sharkflow/data/api/dto/user/UpdateUserAvatarDto.kt](/sharkflow/data/api/dto/user/UpdateUserAvatarDto.kt)                                                 | Kotlin   |   12 |       0 |     4 |    16 |
| [sharkflow/data/api/dto/user/UserDto.kt](/sharkflow/data/api/dto/user/UserDto.kt)                                                                         | Kotlin   |   10 |       0 |     3 |    13 |
| [sharkflow/data/api/dto/user/UserResponseDto.kt](/sharkflow/data/api/dto/user/UserResponseDto.kt)                                                         | Kotlin   |   -8 |       0 |    -3 |   -11 |
| [sharkflow/data/local/db/entities/UserEntity.kt](/sharkflow/data/local/db/entities/UserEntity.kt)                                                         | Kotlin   |   -1 |       0 |     0 |    -1 |
| [sharkflow/data/mapper/UserMapper.kt](/sharkflow/data/mapper/UserMapper.kt)                                                                               | Kotlin   |   17 |       0 |     2 |    19 |
| [sharkflow/data/network/AuthInterceptor.kt](/sharkflow/data/network/AuthInterceptor.kt)                                                                   | Kotlin   |    0 |       0 |    -3 |    -3 |
| [sharkflow/data/repository/AuthRepositoryImpl.kt](/sharkflow/data/repository/AuthRepositoryImpl.kt)                                                       | Kotlin   |  -50 |      -1 |    -9 |   -60 |
| [sharkflow/data/repository/CloudinaryRepositoryImpl.kt](/sharkflow/data/repository/CloudinaryRepositoryImpl.kt)                                           | Kotlin   |  -65 |       0 |   -13 |   -78 |
| [sharkflow/data/repository/LanguageRepositoryImpl.kt](/sharkflow/data/repository/LanguageRepositoryImpl.kt)                                               | Kotlin   |  -27 |       0 |    -7 |   -34 |
| [sharkflow/data/repository/RegisterRepositoryImpl.kt](/sharkflow/data/repository/RegisterRepositoryImpl.kt)                                               | Kotlin   |  -44 |       0 |    -5 |   -49 |
| [sharkflow/data/repository/SecureTokenPreferenceImpl.kt](/sharkflow/data/repository/SecureTokenPreferenceImpl.kt)                                         | Kotlin   |  -13 |       0 |    -4 |   -17 |
| [sharkflow/data/repository/TokenRepositoryImpl.kt](/sharkflow/data/repository/TokenRepositoryImpl.kt)                                                     | Kotlin   |  -19 |       0 |    -6 |   -25 |
| [sharkflow/data/repository/UserRepositoryImpl.kt](/sharkflow/data/repository/UserRepositoryImpl.kt)                                                       | Kotlin   | -130 |       0 |   -14 |  -144 |
| [sharkflow/data/repository/combined/UserRepositoryCombinedImpl.kt](/sharkflow/data/repository/combined/UserRepositoryCombined.kt)                         | Kotlin   |   81 |       0 |    12 |    93 |
| [sharkflow/data/repository/local/UserLocalRepositoryImpl.kt](/sharkflow/data/repository/local/UserLocalRepository.kt)                                     | Kotlin   |   26 |       0 |     6 |    32 |
| [sharkflow/data/repository/remote/AuthRepositoryImpl.kt](/sharkflow/data/repository/remote/AuthRepositoryImpl.kt)                                         | Kotlin   |   41 |       1 |     7 |    49 |
| [sharkflow/data/repository/remote/CloudinaryRepositoryImpl.kt](/sharkflow/data/repository/remote/CloudinaryRepositoryImpl.kt)                             | Kotlin   |   65 |       0 |    13 |    78 |
| [sharkflow/data/repository/remote/LanguageRepositoryImpl.kt](/sharkflow/data/repository/remote/LanguageRepositoryImpl.kt)                                 | Kotlin   |   27 |       0 |     7 |    34 |
| [sharkflow/data/repository/remote/RegisterRepositoryImpl.kt](/sharkflow/data/repository/remote/RegisterRepositoryImpl.kt)                                 | Kotlin   |   33 |       0 |     5 |    38 |
| [sharkflow/data/repository/remote/SecureTokenPreferenceImpl.kt](/sharkflow/data/repository/remote/SecureTokenPreferenceImpl.kt)                           | Kotlin   |   14 |       0 |     4 |    18 |
| [sharkflow/data/repository/remote/ThemeRepositoryImpl.kt](/sharkflow/data/repository/remote/ThemeRepositoryImpl.kt)                                       | Kotlin   |   16 |       0 |     4 |    20 |
| [sharkflow/data/repository/remote/TokenRepositoryImpl.kt](/sharkflow/data/repository/remote/TokenRepositoryImpl.kt)                                       | Kotlin   |   19 |       0 |     5 |    24 |
| [sharkflow/data/repository/remote/UserRepositoryImpl.kt](/sharkflow/data/repository/remote/UserRepositoryImpl.kt)                                         | Kotlin   |   52 |       0 |    12 |    64 |
| [sharkflow/di/DatabaseModule.kt](/sharkflow/di/DatabaseModule.kt)                                                                                         | Kotlin   |    0 |       0 |     1 |     1 |
| [sharkflow/di/RepositoryModule.kt](/sharkflow/di/RepositoryModule.kt)                                                                                     | Kotlin   |    5 |       0 |     1 |     6 |
| [sharkflow/di/SecureTokenStorageModule.kt](/sharkflow/di/SecureTokenStorageModule.kt)                                                                     | Kotlin   |  -24 |       0 |    -5 |   -29 |
| [sharkflow/di/StorageModule.kt](/sharkflow/di/StorageModule.kt)                                                                                           | Kotlin   |   24 |       0 |     5 |    29 |
| [sharkflow/domain/manager/UserManager.kt](/sharkflow/domain/manager/UserManager.kt)                                                                       | Kotlin   |   12 |       0 |     4 |    16 |
| [sharkflow/domain/model/User.kt](/sharkflow/domain/model/User.kt)                                                                                         | Kotlin   |    2 |       0 |     0 |     2 |
| [sharkflow/domain/repository/RegisterRepository.kt](/sharkflow/domain/repository/RegisterRepository.kt)                                                   | Kotlin   |   -2 |       0 |    -1 |    -3 |
| [sharkflow/domain/repository/ThemeRepository.kt](/sharkflow/domain/repository/ThemeRepository.kt)                                                         | Kotlin   |    5 |       0 |     1 |     6 |
| [sharkflow/domain/usecase/GetCurrentLanguageUseCase.kt](/sharkflow/domain/usecase/GetCurrentLanguageUseCase.kt)                                           | Kotlin   |   10 |       0 |     2 |    12 |
| [sharkflow/domain/usecase/auth/CheckTokenUseCase.kt](/sharkflow/domain/usecase/auth/CheckTokenUseCase.kt)                                                 | Kotlin   |    9 |       0 |     3 |    12 |
| [sharkflow/domain/usecase/auth/ConfirmRegistrationCodeUseCase.kt](/sharkflow/domain/usecase/auth/ConfirmRegistrationCodeUseCase.kt)                       | Kotlin   |   10 |       0 |     3 |    13 |
| [sharkflow/domain/usecase/auth/LoginUseCase.kt](/sharkflow/domain/usecase/auth/LoginUseCase.kt)                                                           | Kotlin   |    1 |       0 |    -6 |    -5 |
| [sharkflow/domain/usecase/auth/LogoutUserUseCase.kt](/sharkflow/domain/usecase/auth/LogoutUserUseCase.kt)                                                 | Kotlin   |   15 |       0 |     3 |    18 |
| [sharkflow/domain/usecase/auth/RefreshTokenUseCase.kt](/sharkflow/domain/usecase/auth/RefreshTokenUseCase.kt)                                             | Kotlin   |   -3 |       0 |     0 |    -3 |
| [sharkflow/domain/usecase/auth/RegisterUserUseCase.kt](/sharkflow/domain/usecase/auth/RegisterUserUseCase.kt)                                             | Kotlin   |   15 |       0 |     3 |    18 |
| [sharkflow/domain/usecase/theme/GetThemeUseCase.kt](/sharkflow/domain/usecase/theme/GetThemeUseCase.kt)                                                   | Kotlin   |    8 |       0 |     2 |    10 |
| [sharkflow/domain/usecase/theme/SetThemeUseCase.kt](/sharkflow/domain/usecase/theme/SetThemeUseCase.kt)                                                   | Kotlin   |   10 |       0 |     2 |    12 |
| [sharkflow/domain/usecase/user/ConfirmDeleteUserUseCase.kt](/sharkflow/domain/usecase/user/ConfirmDeleteUserUseCase.kt)                                   | Kotlin   |  -20 |       0 |    -3 |   -23 |
| [sharkflow/domain/usecase/user/ConfirmRegistrationCodeUseCase.kt](/sharkflow/domain/usecase/user/ConfirmRegistrationCodeUseCase.kt)                       | Kotlin   |  -24 |       0 |    -3 |   -27 |
| [sharkflow/domain/usecase/user/DeleteUserAvatarUseCase.kt](/sharkflow/domain/usecase/user/DeleteUserAvatarUseCase.kt)                                     | Kotlin   |  -10 |       0 |    -3 |   -13 |
| [sharkflow/domain/usecase/user/LoadUserUseCase.kt](/sharkflow/domain/usecase/user/LoadUserUseCase.kt)                                                     | Kotlin   |  -11 |       0 |    -3 |   -14 |
| [sharkflow/domain/usecase/user/LogoutUserUseCase.kt](/sharkflow/domain/usecase/user/LogoutUserUseCase.kt)                                                 | Kotlin   |  -13 |       0 |    -3 |   -16 |
| [sharkflow/domain/usecase/user/RegisterUserUseCase.kt](/sharkflow/domain/usecase/user/RegisterUserUseCase.kt)                                             | Kotlin   |  -15 |       0 |    -3 |   -18 |
| [sharkflow/domain/usecase/user/RequestDeleteUserCodeUseCase.kt](/sharkflow/domain/usecase/user/RequestDeleteUserCodeUseCase.kt)                           | Kotlin   |  -10 |       0 |    -3 |   -13 |
| [sharkflow/domain/usecase/user/RequestUpdateUserCodeUseCase.kt](/sharkflow/domain/usecase/user/RequestUpdateUserCodeUseCase.kt)                           | Kotlin   |  -10 |       0 |    -3 |   -13 |
| [sharkflow/domain/usecase/user/UpdateUserAvatarUseCase.kt](/sharkflow/domain/usecase/user/UpdateUserAvatarUseCase.kt)                                     | Kotlin   |  -14 |       0 |    -3 |   -17 |
| [sharkflow/domain/usecase/user/UpdateUserUseCase.kt](/sharkflow/domain/usecase/user/UpdateUserUseCase.kt)                                                 | Kotlin   |  -20 |       0 |    -5 |   -25 |
| [sharkflow/domain/usecase/user/UploadUserAvatarUseCase.kt](/sharkflow/domain/usecase/user/UploadUserAvatarUseCase.kt)                                     | Kotlin   |  -16 |       0 |    -2 |   -18 |
| [sharkflow/domain/usecase/user/delete/ConfirmDeleteUserUseCase.kt](/sharkflow/domain/usecase/user/delete/ConfirmDeleteUserUseCase.kt)                     | Kotlin   |   17 |       0 |     3 |    20 |
| [sharkflow/domain/usecase/user/delete/DeleteUserAccountUseCase.kt](/sharkflow/domain/usecase/user/delete/DeleteUserAccountUseCase.kt)                     | Kotlin   |   14 |       0 |     3 |    17 |
| [sharkflow/domain/usecase/user/delete/DeleteUserAvatarUseCase.kt](/sharkflow/domain/usecase/user/delete/DeleteUserAvatarUseCase.kt)                       | Kotlin   |   13 |       0 |     3 |    16 |
| [sharkflow/domain/usecase/user/delete/RequestDeleteUserCodeUseCase.kt](/sharkflow/domain/usecase/user/delete/RequestDeleteUserCodeUseCase.kt)             | Kotlin   |   10 |       0 |     3 |    13 |
| [sharkflow/domain/usecase/user/get/LoadUserUseCase.kt](/sharkflow/domain/usecase/user/get/LoadUserUseCase.kt)                                             | Kotlin   |   13 |       0 |     2 |    15 |
| [sharkflow/domain/usecase/user/init/InitializeUserSessionUseCase.kt](/sharkflow/domain/usecase/user/init/InitializeUserSessionUseCase.kt)                 | Kotlin   |   24 |       0 |     2 |    26 |
| [sharkflow/domain/usecase/user/update/RequestUpdateUserCodeUseCase.kt](/sharkflow/domain/usecase/user/update/RequestUpdateUserCodeUseCase.kt)             | Kotlin   |   10 |       0 |     3 |    13 |
| [sharkflow/domain/usecase/user/update/UpdateUserAvatarUseCase.kt](/sharkflow/domain/usecase/user/update/UpdateUserAvatarUseCase.kt)                       | Kotlin   |   14 |       0 |     3 |    17 |
| [sharkflow/domain/usecase/user/update/UpdateUserUseCase.kt](/sharkflow/domain/usecase/user/update/UpdateUserUseCase.kt)                                   | Kotlin   |   15 |       0 |     3 |    18 |
| [sharkflow/domain/usecase/user/update/UploadUserAvatarUseCase.kt](/sharkflow/domain/usecase/user/update/UploadUserAvatarUseCase.kt)                       | Kotlin   |   16 |       0 |     3 |    19 |
| [sharkflow/presentation/screens/auth/viewmodel/AuthStateViewModel.kt](/sharkflow/presentation/screens/auth/viewmodel/AuthStateViewModel.kt)               | Kotlin   |   -2 |       0 |    -1 |    -3 |
| [sharkflow/presentation/screens/auth/viewmodel/ConfirmationCodeViewModel.kt](/sharkflow/presentation/screens/auth/viewmodel/ConfirmationCodeViewModel.kt) | Kotlin   |  -18 |       0 |    -2 |   -20 |
| [sharkflow/presentation/screens/auth/viewmodel/LoginViewModel.kt](/sharkflow/presentation/screens/auth/viewmodel/LoginViewModel.kt)                       | Kotlin   |  -14 |       0 |    -2 |   -16 |
| [sharkflow/presentation/screens/auth/viewmodel/RegisterViewModel.kt](/sharkflow/presentation/screens/auth/viewmodel/RegisterViewModel.kt)                 | Kotlin   |  -14 |       0 |    -3 |   -17 |
| [sharkflow/presentation/screens/common/SplashScreen.kt](/sharkflow/presentation/screens/common/SplashScreen.kt)                                           | Kotlin   |    1 |       0 |     1 |     2 |
| [sharkflow/presentation/screens/profile/ProfileScreen.kt](/sharkflow/presentation/screens/profile/ProfileScreen.kt)                                       | Kotlin   |  -12 |       0 |    -1 |   -13 |
| [sharkflow/presentation/screens/profile/components/ProfileAvatar.kt](/sharkflow/presentation/screens/profile/components/ProfileAvatar.kt)                 | Kotlin   |  -34 |       0 |    -1 |   -35 |
| [sharkflow/presentation/screens/profile/viewmodel/UserProfileViewModel.kt](/sharkflow/presentation/screens/profile/viewmodel/UserProfileViewModel.kt)     | Kotlin   |  -47 |       0 |    -3 |   -50 |
| [sharkflow/utils/ApiUtils.kt](/sharkflow/utils/ApiUtils.kt)                                                                                               | Kotlin   |   33 |       0 |     3 |    36 |
| [sharkflow/utils/ResultExt.kt](/sharkflow/utils/ResultExt.kt)                                                                                             | Kotlin   |    3 |       0 |     2 |     5 |
| [sharkflow/viewmodel/AppViewModel.kt](/sharkflow/viewmodel/AppViewModel.kt)                                                                               | Kotlin   |   -3 |       0 |     0 |    -3 |
| [sharkflow/viewmodel/BaseViewModel.kt](/sharkflow/viewmodel/BaseViewModel.kt)                                                                             | Kotlin   |   39 |       0 |     6 |    45 |
| [sharkflow/viewmodel/ThemeViewModel.kt](/sharkflow/viewmodel/ThemeViewModel.kt)                                                                           | Kotlin   |   29 |       0 |     7 |    36 |

[Summary](results.md) / [Details](details.md) / [Diff Summary](diff.md) / Diff Details