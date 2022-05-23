from http import HTTPStatus
import requests

# reInitialize all services and check if they return 201 status code
# Use get balance end point of customer-service to get a Customer and check the initial data are properly read and assigned to the customer

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

    # Customer -999 must not be present so 404 HttpStatus must be returned
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
        test_result = 'Fail'

    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
