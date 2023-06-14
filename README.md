
# EComm. App

This is an e-commerce that app allows the user to shop online.

Users can able to see all the listed products without Logged In with features like sorting that list and filtering products according to categories.

Users can add products to the cart and remove them from it if he/she wants. After adding to the cart, the user can checkout.

Note:- 
The database is published on the in.000webhost.com (free version) and because of that server bandwidth is less/limited, which causes buffering issues in the app. 


## Preview

Link of preview

https://www.veed.io/view/01c5e7ce-4a09-4119-b629-690ddc8e2593?panel=share

## API Reference

### For products - 

#### To get all products

```http
GET - https://fakestoreapi.com/products
```



#### To get all electronics products  
```http
GET - https://fakestoreapi.com/products/category/electronics
```

#### To get all jewelry products
```http
GET - https://fakestoreapi.com/products/category/jewelery
```
#### To get all men's clothing products

```http
GET - https://fakestoreapi.com/products/category/men's%20clothing
```
#### To get all women's clothing products 

```http
GET - https://fakestoreapi.com/products/category/women's%20clothing
```


#### To sort all products by old to new

```http
GET - https://fakestoreapi.com/products?sort=asc
````


#### To sort all  products by new to old
```http
GET - https://fakestoreapi.com/products?sort=desc
````
#### For user signup
```http
POST -  https://ecommdot.000webhostapp.com/api/ecomm.php
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `signup`      | `string` | **Required**. to call this method |
| `name`      | `string` | **Required**. to insert name of the user in the table|
| `phone`      | `string` | **Required**. to insert phone no of the user in the table |
| `email`      | `string` | **Required**. to insert email-id of the user in the table |
| `password`      | `string` | **Required**. to insert password of the user in the table |



#### For user OTP verification
```http
POST -  https://ecommdot.000webhostapp.com/api/ecomm.php
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `checkotp`      | `string` | **Required**. to call this method |
| `email`      | `string` | **Required**. to select row in the table |
| `code`      | `string` | **Required**. to check entered otp code is right or not |



#### For verifying the user's login credentials
```http
POST -  https://ecommdot.000webhostapp.com/api/ecomm.php
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `checklogin`      | `string` | **Required**. to call this method |
| `email`      | `string` | **Required**. to verify email of the user |
| `password`      | `string` | **Required**. to verify password of the user |



#### To get the user's id and name from the database
```http
POST -  https://ecommdot.000webhostapp.com/api/ecomm.php
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `getdata`      | `string` | **Required**. to call this method |
| `email`      | `string` | **Required**. to get user id from email id of the user |



#### For user signup
```http
POST -  https://ecommdot.000webhostapp.com/api/ecomm.php
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `signup`      | `string` | **Required**. to call this method |
| `name`      | `string` | **Required**. name of the user |
| `phone`      | `string` | **Required**. phone no of the user |
| `email`      | `string` | **Required**. email-id of the user |
| `password`      | `string` | **Required**. password of the user |


#### For user signup
```http
POST -  https://ecommdot.000webhostapp.com/api/ecomm.php
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `signup`      | `string` | **Required**. to call this method |
| `name`      | `string` | **Required**. name of the user |
| `phone`      | `string` | **Required**. phone no of the user |
| `email`      | `string` | **Required**. email-id of the user |
| `password`      | `string` | **Required**. password of the user |


#### To add a product to the cart
```http
POST -  https://ecommdot.000webhostapp.com/api/ecomm.php
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `addtocart`      | `string` | **Required**. to call this method |
| `uid`      | `string` | **Required**. to insert user id of the user in the cart table  |
| `pid`      | `string` | **Required**. to insert product id of the product in the cart table  |
| `pimg`      | `string` | **Required**. to insert product image of the product in the cart table  |
| `title`      | `string` | **Required**. to insert product title of the product in the cart table  |
| `price`      | `string` | **Required**. to insert product price of the product in the cart table  |
| `quantity`      | `string` | **Required**. to insert product quantity of the product in the cart table  |
| `category`      | `string` | **Required**. to insert product category of the product in the cart table  |

#### To get cart items
```http
POST -  https://ecommdot.000webhostapp.com/api/ecomm.php
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `getCart`      | `string` | **Required**. to call this method |
| `uId`      | `string` | **Required**. to get cart of the user by user-Id |

#### To delete the item from the cart
```http
POST -  https://ecommdot.000webhostapp.com/api/ecomm.php
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `deleteItem`      | `string` | **Required**. to call this method |
| `uId`      | `string` | **Required**. to delete product from cart contains uId |
| `pId`      | `string` | **Required**. to delete product from cart contains pId |

