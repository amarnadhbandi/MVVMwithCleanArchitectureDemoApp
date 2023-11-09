# MVVMwithCleanArchitectureDemoApp
Concept of Design Android App by using MVVM with Clean Architecture with Sample Api 

BASE_URL need to update before checking changes 
navigate to com.demo.app.utils.Constants replace https://YourUrl with your URL  (or) we can cofigure in postman with below api response sample 

const val BASE_URL = "https://YourUrl" 

MVVM with Clean Architecture:

UI 
Presenter/ViewModel     -> Presentation Layer    


UseCases + Models   ->  Business/Domain Layer


Repository, 
DataSource                     -> Data Layer

Notes: 

Chosen Clean Architecture to implement API
Focused more into network API logic and workflow
Manual DI by using AppContainer
Implemented local repository RoomDB
Implemented remote api with caching Retrofit
Basically two Handlers used
a. UI - CountriesLisViewState 
b. NetworkApi - ResultHandler
 Implementation – error handling is very important
Used DiffUtil for optimization
Used Fragment, Usecase, list adapter
Use domain layer with mapper, submitList, manual DI
Orientation support with single screen Fragment 
Added few debug logs to understand flow
Missing Unit Test case, might need some more time 

Reponse sample : 
[
{
    "capital": "Andorra la Vella",
    "code": "AD",
    "currency": {
      "code": "EUR",
      "name": "Euro",
      "symbol": "€"
    },
    "flag": "SVG File",
    "language": {
      "code": "ca",
      "name": "Catalan"
    },
    "name": "Andorra",
    "region": "EU"
  },
  {
    "capital": "Luanda",
    "code": "AO",
    "currency": {
      "code": "AOA",
      "name": "Angolan kwanza",
      "symbol": "Kz"
    },
    "flag": "SVG File",
    "language": {
      "code": "pt",
      "name": "Portuguese"
    },
    "name": "Angola",
    "region": "AF"
  } 
]
