from http import HTTPStatus
import requests



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

    http_response = requests.post(
        "http://localhost:8080/refillItem", json={"restId": 102, "itemId": 1, "qty": 2})
    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail4'

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
