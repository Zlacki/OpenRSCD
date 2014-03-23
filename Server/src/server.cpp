#include <iostream>
#include <boost/bind.hpp>
#include <boost/lexical_cast.hpp>
#include "server.hpp"
#include "model/collections.hpp"
#include "model/player.hpp"
#include "model/region.hpp"

namespace RSC {
	using namespace IO;
	using namespace Model;
	std::shared_ptr<Collections> collections;
	std::shared_ptr<RegionManager> regionManager;

	Server::Server(std::size_t io_service_pool_size) : io_service_pool_(io_service_pool_size),
			signals_(io_service_pool_.get_io_service()), acceptor_(io_service_pool_.get_io_service()),
			new_connection_()
	{
		signals_.add(SIGINT);
		signals_.add(SIGTERM);
#if defined(SIGQUIT)
		signals_.add(SIGQUIT);
#endif
		signals_.async_wait(boost::bind(&Server::handle_stop, this));

		boost::asio::ip::tcp::resolver resolver(acceptor_.get_io_service());
		boost::asio::ip::tcp::resolver::query query("0.0.0.0", "43594");
		boost::asio::ip::tcp::endpoint endpoint = *resolver.resolve(query);
		acceptor_.open(endpoint.protocol());
		acceptor_.set_option(boost::asio::ip::tcp::acceptor::reuse_address(true));
		acceptor_.set_option(boost::asio::ip::tcp::no_delay(true));
		acceptor_.bind(endpoint);
		acceptor_.listen();

		start_accept();
	}

	void Server::run()
	{
		io_service_pool_.run();
	}

	void Server::start_accept()
	{
		new_connection_.reset(new connection(io_service_pool_.get_io_service()));
		acceptor_.async_accept(new_connection_->socket(),
			boost::bind(&Server::handle_accept, this,
			boost::asio::placeholders::error));
	}

	void Server::handle_accept(const boost::system::error_code& e)
	{
		if (!e) {
			for(int i = 0; i < 2000; i++)
				if(collections->getPlayer(i) == NULL) {
					new_connection_->setIndex(i);
					std::shared_ptr<Player> player = std::make_shared<Player>(new_connection_, i);
					std::shared_ptr<NetSender> netSender = std::make_shared<NetSender>(player);
					collections->addPlayer(player);
					player->setNetSender(netSender);
					break;
				}
			new_connection_->start();
		}

		start_accept();
	}

	void Server::handle_stop()
	{
		io_service_pool_.stop();
	}
}

int main(int argc, char *argv[])
{
	try {
		if (argc != 2) {
			std::cerr << "Usage: openrscd <threads>\n" << std::endl;
			std::cerr << "threads: Number of threads to run I/O on" << std::endl;
			return 1;
		}

		std::size_t num_threads = boost::lexical_cast<std::size_t>(argv[1]);
		RSC::Server s(num_threads);

		std::cout << "Loading data definitions..." << std::endl;
		RSC::collections = std::make_shared<RSC::Collections>();
		RSC::collections->setDefinitionHandler(std::make_shared<RSC::DefinitionHandler>());
		RSC::collections->getDefinitionHandler()->load();
		std::cout << "Definitions loaded." << std::endl << std::endl;
		std::cout << "Loading location definitions..." << std::endl;
		RSC::regionManager = std::make_shared<RSC::RegionManager>();
		std::cout << "Locations loaded" << std::endl << std::endl;

		s.run();
	} catch(std::exception &e) {
		std::cerr << "Exception: " << e.what() << std::endl;
	}
}
