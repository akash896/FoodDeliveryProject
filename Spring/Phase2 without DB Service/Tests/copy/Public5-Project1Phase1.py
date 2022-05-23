from http import HTTPStatus
import requests


def test():
    test_result = 'Pass'



    # Reinitialize Restaurant service
    http_response = requests.post("http://localhost:8080/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail 1'

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail 2'

    http_response = requests.post("http://localhost:8082/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail 3'

    http_response = requests.get("http://localhost:8082/balance/301")
    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail 4'

    res_body = http_response.json()
    cust_id = res_body.get("custId")
    cust_balance = res_body.get("balance")
    if (cust_id != 301):
        test_result = 'Fail 5'

    if(cust_balance != 2000.0):
        test_result = 'Fail 6'

    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
