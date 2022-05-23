from http import HTTPStatus
import requests

"""
reInitialize all services and check if they return 201 status code
Check deductBalance and addBalance end point of customer-service are working properly or not

# reInitialize all services and check if they return 201 status code
# use addBalance service of wallet to add money 
 check if the current balance increased by that amount or not
# use deductBalance service of wallet to withdraw money 
 check if the current balance decreased by that amount or not


"""
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

    # Customer 301 must br present and amount must be 2000.0
    http_response = requests.get("http://localhost:8082/balance/301")
    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail4'

    res_body = http_response.json()
    cust_id = res_body.get("custId")
    cust_balance = res_body.get("balance")
    #checking if the customer received in response is correct customer
    if (cust_id != 301):
        test_result = 'Fail5'

    # check that the balance present for the customer is 2000 (which is the initial amount of customer)
    if(cust_balance != 2000.0):
        test_result = 'Fail6'


    ############### Use addBalance end point ############################
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
    # checking if the customer received in response is correct customer
    if (cust_id != 301):
        test_result = 'Fail9'

    # check that the balance present for the customer is 2000 (which is the initial amount of customer)
    if (cust_balance != 2200.0):
        test_result = 'Fail10'

        ############### Use deductBalance end point ############################
    http_response = requests.post(
        "http://localhost:8082/deductBalance", json={"custId": 301, "amount": 300})
    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail11'
    # Customer 301 must be present and amount must be 1900.0
    http_response = requests.get("http://localhost:8082/balance/301")
    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail12'

    res_body = http_response.json()
    cust_id = res_body.get("custId")
    cust_balance = res_body.get("balance")
    # checking if the customer received in response is correct customer
    if (cust_id != 301):
        test_result = 'Fail13'

    # check that the balance present for the customer is 2000 (which is the initial amount of customer)
    if (cust_balance != 1900.0):
        test_result = 'Fail14'

    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
