# Diff Details

Date : 2025-10-20 02:04:55

Directory c:
\\Users\\karen\\AndroidStudioProjects\\SharkFlow\\app\\src\\main\\java\\com\\example\\sharkflow

Total : 29 files, 139 codes, 0 comments, 35 blanks, all 174 lines

[Summary](results.md) / [Details](details.md) / [Diff Summary](diff.md) / Diff Details

## Files

| filename                                                                                                                                              | language | code | comment | blank | total |
|:------------------------------------------------------------------------------------------------------------------------------------------------------|:---------|-----:|--------:|------:|------:|
| [sharkflow/data/api/dto/user/UpdateUserAvatarRequestDto.kt](/sharkflow/data/api/dto/user/UpdateAvatarRequestDto.kt)                                   | Kotlin   |    4 |       0 |     2 |     6 |
| [sharkflow/data/api/dto/user/UpdateUserDto.kt](/sharkflow/data/api/dto/user/UpdateUserDto.kt)                                                         | Kotlin   |    5 |       0 |     2 |     7 |
| [sharkflow/data/repository/TokenRepositoryImpl.kt](/sharkflow/data/repository/TokenRepositoryImpl.kt)                                                 | Kotlin   |    0 |       0 |     1 |     1 |
| [sharkflow/data/repository/UserRepositoryImpl.kt](/sharkflow/data/repository/UserRepositoryImpl.kt)                                                   | Kotlin   |   15 |       0 |     4 |    19 |
| [sharkflow/domain/repository/UserRepository.kt](/sharkflow/domain/repository/UserRepository.kt)                                                       | Kotlin   |    8 |       0 |     2 |    10 |
| [sharkflow/domain/usecase/ClearTokensUseCase.kt](/sharkflow/domain/usecase/ClearTokensUseCase.kt)                                                     | Kotlin   |  -10 |       0 |    -2 |   -12 |
| [sharkflow/domain/usecase/ConfirmRegistrationCodeUseCase.kt](/sharkflow/domain/usecase/ConfirmRegistrationCodeUseCase.kt)                             | Kotlin   |  -24 |       0 |    -3 |   -27 |
| [sharkflow/domain/usecase/LoginUseCase.kt](/sharkflow/domain/usecase/LoginUseCase.kt)                                                                 | Kotlin   |  -28 |       0 |    -8 |   -36 |
| [sharkflow/domain/usecase/RefreshTokenUseCase.kt](/sharkflow/domain/usecase/RefreshTokenUseCase.kt)                                                   | Kotlin   |  -18 |       0 |    -3 |   -21 |
| [sharkflow/domain/usecase/RegisterUserUseCase.kt](/sharkflow/domain/usecase/RegisterUserUseCase.kt)                                                   | Kotlin   |  -15 |       0 |    -3 |   -18 |
| [sharkflow/domain/usecase/SaveTokensUseCase.kt](/sharkflow/domain/usecase/SaveTokensUseCase.kt)                                                       | Kotlin   |  -10 |       0 |    -2 |   -12 |
| [sharkflow/domain/usecase/UpdateUserUseCase.kt](/sharkflow/domain/usecase/UpdateUserUseCase.kt)                                                       | Kotlin   |  -24 |       0 |    -3 |   -27 |
| [sharkflow/domain/usecase/UploadUserAvatarUseCase.kt](/sharkflow/domain/usecase/UploadUserAvatarUseCase.kt)                                           | Kotlin   |  -16 |       0 |    -3 |   -19 |
| [sharkflow/domain/usecase/auth/ClearTokensUseCase.kt](/sharkflow/domain/usecase/auth/ClearTokensUseCase.kt)                                           | Kotlin   |   10 |       0 |     2 |    12 |
| [sharkflow/domain/usecase/auth/LoginUseCase.kt](/sharkflow/domain/usecase/auth/LoginUseCase.kt)                                                       | Kotlin   |   27 |       0 |     8 |    35 |
| [sharkflow/domain/usecase/auth/RefreshTokenUseCase.kt](/sharkflow/domain/usecase/auth/RefreshTokenUseCase.kt)                                         | Kotlin   |   18 |       0 |     3 |    21 |
| [sharkflow/domain/usecase/auth/SaveTokensUseCase.kt](/sharkflow/domain/usecase/auth/SaveTokensUseCase.kt)                                             | Kotlin   |   10 |       0 |     2 |    12 |
| [sharkflow/domain/usecase/user/ConfirmDeleteUserUseCase.kt](/sharkflow/domain/usecase/user/ConfirmDeleteUserUseCase.kt)                               | Kotlin   |   20 |       0 |     3 |    23 |
| [sharkflow/domain/usecase/user/ConfirmRegistrationCodeUseCase.kt](/sharkflow/domain/usecase/user/ConfirmRegistrationCodeUseCase.kt)                   | Kotlin   |   24 |       0 |     3 |    27 |
| [sharkflow/domain/usecase/user/DeleteUserAvatarUseCase.kt](/sharkflow/domain/usecase/user/DeleteUserAvatarUseCase.kt)                                 | Kotlin   |   10 |       0 |     3 |    13 |
| [sharkflow/domain/usecase/user/LoadUserUseCase.kt](/sharkflow/domain/usecase/user/LoadUserUseCase.kt)                                                 | Kotlin   |   11 |       0 |     3 |    14 |
| [sharkflow/domain/usecase/user/LogoutUserUseCase.kt](/sharkflow/domain/usecase/user/LogoutUserUseCase.kt)                                             | Kotlin   |   13 |       0 |     3 |    16 |
| [sharkflow/domain/usecase/user/RegisterUserUseCase.kt](/sharkflow/domain/usecase/user/RegisterUserUseCase.kt)                                         | Kotlin   |   15 |       0 |     3 |    18 |
| [sharkflow/domain/usecase/user/RequestDeleteUserCodeUseCase.kt](/sharkflow/domain/usecase/user/RequestDeleteUserCodeUseCase.kt)                       | Kotlin   |   10 |       0 |     3 |    13 |
| [sharkflow/domain/usecase/user/RequestUpdateUserCodeUseCase.kt](/sharkflow/domain/usecase/user/RequestUpdateUserCodeUseCase.kt)                       | Kotlin   |   10 |       0 |     3 |    13 |
| [sharkflow/domain/usecase/user/UpdateUserAvatarUseCase.kt](/sharkflow/domain/usecase/user/UpdateUserAvatarUseCase.kt)                                 | Kotlin   |   14 |       0 |     3 |    17 |
| [sharkflow/domain/usecase/user/UpdateUserUseCase.kt](/sharkflow/domain/usecase/user/UpdateUserUseCase.kt)                                             | Kotlin   |   20 |       0 |     5 |    25 |
| [sharkflow/domain/usecase/user/UploadUserAvatarUseCase.kt](/sharkflow/domain/usecase/user/UploadUserAvatarUseCase.kt)                                 | Kotlin   |   16 |       0 |     2 |    18 |
| [sharkflow/presentation/screens/profile/viewmodel/UserProfileViewModel.kt](/sharkflow/presentation/screens/profile/viewmodel/UserProfileViewModel.kt) | Kotlin   |   24 |       0 |     2 |    26 |

[Summary](results.md) / [Details](details.md) / [Diff Summary](diff.md) / Diff Details