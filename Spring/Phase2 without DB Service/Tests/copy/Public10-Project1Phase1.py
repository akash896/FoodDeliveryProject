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

    http_response = requests.get("http://localhost:8082/balance/301")
    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail4'

    res_body = http_response.json()
    cust_id = res_body.get("custId")
    cust_balance = res_body.get("balance")
    #checking if the customer received in response is correct customer
    if (cust_id != 301):
        test_result = 'Fail5'

    if(cust_balance != 2000.0):
        test_result = 'Fail6'


    http_response = requests.post(
        "http://localhost:8082/addBalance", json={"custId": 301, "amount":200})
    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail7'
    # Customer 301 must br present and amount must be 2200.0
    http_response = requests.get("http://localhost:8082/balance/301")
    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail8'

    res_body = http_response.json()
    cust_id = res_body.get("custId")
    cust_balance = res_body.get("balance")
    if (cust_id != 301):
        test_result = 'Fail9'

    if (cust_balance != 2200.0):
        test_result = 'Fail10'

    http_response = requests.post(
        "http://localhost:8082/deductBalance", json={"custId": 301, "amount": 300})
    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail11'
    http_response = requests.get("http://localhost:8082/balance/301")
    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail12'

    res_body = http_response.json()
    cust_id = res_body.get("custId")
    cust_balance = res_body.get("balance")
    if (cust_id != 301):
        test_result = 'Fail13'

    if (cust_balance != 1900.0):
        test_result = 'Fail14'

    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
