from http import HTTPStatus
from threading import Thread
import requests

# Check if only one order is assigned when multiple concurrent requests
# for order comes, when only one delivery agent is available

# RESTAURANT SERVICE    : http://localhost:8080
# DELIVERY SERVICE      : http://localhost:8081
# WALLET SERVICE        : http://localhost:8082


def t1(result):  # First concurrent request
    # Agent 201 sign in
    http_response = requests.post("http://localhost:8081/agentSignIn", json={"agentId": 201})
    result["1"] = http_response


def t2(result):  # Second concurrent request
    # Agent 201 sign in
    http_response = requests.post("http://localhost:8081/agentSignIn", json={"agentId": 202})
    result["2"] = http_response


def test():

    result = {}

    # Reinitialize Restaurant service
    http_response = requests.post("http://localhost:8080/reInitialize")

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    # Reinitialize Wallet service
    http_response = requests.post("http://localhost:8082/reInitialize")

    # Agent 201 sign in
    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 201})

    if(http_response.status_code != HTTPStatus.CREATED):
        return 'Fail1'

    ### Parallel Execution Begins ###
    thread1 = Thread(target=t1, kwargs={"result": result})
    thread2 = Thread(target=t2, kwargs={"result": result})

    thread1.start()
    thread2.start()

    thread1.join()
    thread2.join()

    ### Parallel Execution Ends ###

    status_code1 = result["1"].status_code

    status_code2 = result["2"].status_code

    if (status_code1 != status_code2 and status_code2 != HTTPStatus.CREATED):
        return "Fail2"

    return 'Pass'


if __name__ == "__main__":
    print(test())