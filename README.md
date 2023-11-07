# MVVMwithCleanArchitectureDemoApp
Concept of Design Android App by using MVVM with Clean Architecture with Sample Api 

BASE_URL need to update before checking changes 
navigate to com.demo.app.utils.Constants replace https://YourUrl with your URL  (or) we can cofigure in postman with below api response sample 

const val BASE_URL = "https://YourUrl" 

MVVM with Clean Architecture:

UI 
Presenter/ViewModel     -> Presentation Layer    


UseCases + Entity   ->  Business/Domain Layer


Repository, 
DataSource                     -> Data Layer


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
