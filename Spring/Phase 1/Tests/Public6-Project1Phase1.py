from http import HTTPStatus
import requests

# Checks if refillItem end point of restaurant service is working properly
# reInitialize all the services
# use refillItem endpoint of restaurant to increase the quantity of item 1 in restaurant 102
# request order such that all the items including the refilled item are required to accept the order
# iforder is accepted then refillItem worked else refillItem did not work properly


# RESTAURANT SERVICE    : http://localhost:8080
# DELIVERY SERVICE      : http://localhost:8081
# WALLET SERVICE        : http://localhost:8082

def test():
    test_result = 'Pass'

    # Reinitialize Restaurant service
    http_response = requests.post("http://localhost:8080/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail1'

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail2'

    # Reinitialize Wallet service
    http_response = requests.post("http://localhost:8082/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail3'

    # adding quantity to the item 1 of restauant 102, so that the below order is accepted only if the
    # refillItem end point of restaurant worked as expected.
    http_response = requests.post(
        "http://localhost:8080/refillItem", json={"restId": 102, "itemId": 1, "qty": 2})
    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail4'

    # Customer 301 requests an order of item 1, quantity 12 from restaurant 102
    http_response = requests.post(
        "http://localhost:8081/requestOrder", json={"custId": 301, "restId": 102, "itemId": 1, "qty": 12})

    res_body = http_response.json()
    order_id = -1
    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail5'

    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
